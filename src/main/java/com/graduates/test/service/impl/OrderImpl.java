package com.graduates.test.service.impl;

import ch.qos.logback.core.status.Status;
import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.*;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.*;
import com.graduates.test.resposity.*;
import com.graduates.test.service.OrderService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderImpl implements OrderService {
    @Autowired
    private UserResposity userRepository;

    @Autowired
    private OrderRespository orderRepository;

    @Autowired
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private PaymentResponsitory paymentRepository;

    @Autowired
    private ShipmentRespository shipmentRepository;
    @Autowired
    private StatusRespository statusRespository;
    @Autowired
    private BookCategoryResposity bookCategoryResposity;

    @Autowired
    private  FeedbackRepository feedbackRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private  PaymentStatusMRepository paymentStatusMRepository;
    @Autowired
    private  VoucherRepository voucherRepository;

    public OrderImpl(UserResposity userRepository, OrderRespository orderRepository, CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderDetailRepository orderDetailRepository, PaymentResponsitory paymentRepository, ShipmentRespository shipmentRepository, StatusRespository statusRespository, BookCategoryResposity bookCategoryResposity, FeedbackRepository feedbackRepository, JwtService jwtService, PaymentStatusMRepository paymentStatusMRepository, VoucherRepository voucherRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentRepository = paymentRepository;
        this.shipmentRepository = shipmentRepository;
        this.statusRespository = statusRespository;
        this.bookCategoryResposity = bookCategoryResposity;
        this.feedbackRepository = feedbackRepository;
        this.jwtService = jwtService;
        this.paymentStatusMRepository = paymentStatusMRepository;
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Order createOrder(String token, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId, String phone, String receivingName, String note,Integer voucherId) throws Exception {
        String username = jwtService.extractUsername(token); // Lấy username từ token

        // Tìm user dựa trên username
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found for username: " + username));

        Integer userId = user.getIdUser(); // Lấy userId từ đối tượng UserEntity


        Cart cart = cartRepository.findByUserIdUser(userId)
                .orElseThrow(() -> new Exception("Cart not found for user ID: " + userId));

        // Tìm payment và shipment từ database
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment not found with ID: " + paymentId));

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new Exception("Shipment not found with ID: " + shipmentId));

        Voucher voucher = null;
        double finalAmount = cart.getTotalAmount();
        if (voucherId != null) {
            voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new Exception("Voucher not found"));
            if (!voucher.getIsActive()) {
                throw new Exception("Voucher is not active and cannot be used.");
            }
            if (voucher.getMaxUsage() <= 0) {
                throw new Exception("Voucher has no remaining uses.");
            }


            // Kiểm tra nếu tổng giá trị đơn hàng thỏa mãn điều kiện voucher
            double totalAmount = cart.getTotalAmount();
            if (totalAmount < voucher.getMinOrderValue()) {
                throw new Exception("Order amount must be above  to use this voucher.");
            }
            double discountValue = voucher.getDiscountValue(); // Phần trăm giảm giá
            finalAmount -= (finalAmount * (discountValue / 100)); // Áp dụng phần trăm giảm giá vào tổng tiền

            // Đảm bảo số tiền cuối cùng không nhỏ hơn 0
            if (finalAmount < 0) {
                finalAmount = 0;
            }


            // Giảm số lượng voucher nếu voucher còn số lượng sử dụng
            voucher.setMaxUsage(voucher.getId() - 1);
            voucherRepository.save(voucher); // Lưu lại voucher sau khi giảm số lượng
        }else {
            voucher = new Voucher();  // Khởi tạo voucher mặc định nếu không có voucherId
            voucher.setDiscountValue(0.0);
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setCart(cart);
        order.setShippingAddress(shippingAddress);
        order.setPayment(payment); // Thiết lập payment
        order.setShipment(shipment); // Thiết lập shipment
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(finalAmount);
        order.setPhone(phone);
        order.setReceivingName(receivingName);
        //  order.setOrderStatus();
        order.setUser(cart.getUser());
        OrderStatus pendingStatus = statusRespository.findByStatus("Processing"); // Tìm trạng thái "Pending" từ bảng Status
        order.setOrderStatus(pendingStatus);
        PaymentStatusM statusM =paymentStatusMRepository.findByStatusName("UNPAID");
        order.setPaymentStatusM(statusM);
        order.setNote(note);
        order.setDeliveryDate(LocalDateTime.now().plusDays(5));
        if (voucherId != null) {
             voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new IllegalArgumentException("Voucher không tồn tại!"));
            order.setVoucher(voucher);
        }
        if (order.getOrderCode() == null || order.getOrderCode().isEmpty()) {
            // Tạo mã đơn hàng theo UUID hoặc quy tắc khác
            order.setOrderCode(generateUniqueOrderCode());
        }
        if(orderRepository.existsByOrderCode(order.getOrderCode())){
            throw  new Exception("order code already exists!");
        }

        // Thiết lập chi tiết đơn hàng
        for (Integer detailId : selectedCartDetailIds) {

            CartDetail cartDetail = cartDetailRepository.findByIdAndCart_IdCart(detailId, cart.getIdCart())

                    .orElseThrow(() -> new Exception("Cart detail not found with ID: " + detailId + " for cart ID: " + cart.getIdCart()));

            if (cartDetail.isPurchased()) {
                throw new Exception("Cannot purchase item: " + cartDetail.getBook().getNameBook() + " because it hasn't valid.");
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(cartDetail.getBook());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getPrice());

            order.addOrderDetail(orderDetail); // Thêm chi tiết đơn hàng vào đơn hàng
            Book book = cartDetail.getBook();
            cartDetail.setPurchased(true);
            if (book.getQuantity() < cartDetail.getQuantity()) {
                throw new Exception("Not enough stock for book: " + book.getNameBook());
            }
            book.setQuantity(book.getQuantity() - cartDetail.getQuantity());
        }


        // Lưu đơn hàng vào database
        return orderRepository.save(order);
    }




    private String generateUniqueOrderCode() {
        return  UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String token) {
        String username = jwtService.extractUsername(token); // Extract username từ token

        // Tìm người dùng dựa trên username (token đã giải mã)
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found for username: " + username));

        // Lấy userId từ đối tượng UserEntity
        Integer userId = user.getIdUser();

        // Lấy danh sách đơn hàng của người dùng theo userId
        List<Order> orders = orderRepository.findByUser_idUserOrderByCreatedAtDesc(userId);

        // Chuyển đổi danh sách Order thành OrderResponse
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Order getOrderById(Integer idOrder) {
        return orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + idOrder));
    }




private OrderResponse convertToOrderResponse(Order order) {
    // Lấy username từ SecurityContextHolder
    String currentUsername = getCurrentUsernameFromContext();

    OrderResponse response = new OrderResponse();
    response.setId(order.getId());
    response.setShipment(order.getShipment().getShippingMethod());
    response.setPayment(order.getPayment().getPaymentMethod());
    response.setCreatedAt(order.getCreatedAt());
    response.setStatus(order.getOrderStatus().getStatus());
    response.setPhone(order.getPhone());
    response.setShippingAdrress(order.getShippingAddress());
    response.setReceiveName(order.getReceivingName());
    response.setNote(order.getNote());
    response.setDeliveryDate(order.getDeliveryDate());
    response.setOrderCode(order.getOrderCode());
    if(order.getVoucher() !=null) {
        response.setVoucher(order.getVoucher().getDiscountValue());
    }else{
        response.setVoucher(0.0);
    }

    List<BookRespone> bookDetails = order.getOrderDetails().stream()
            .map(orderDetail -> {
                BookRespone bookDetail = new BookRespone();
                bookDetail.setOrderDetailId(orderDetail.getIdOrderDetail());
                bookDetail.setIdBook(orderDetail.getBook().getIdBook());
                bookDetail.setNameBook(orderDetail.getBook().getNameBook());
                bookDetail.setAuthor(orderDetail.getBook().getAuthor());
                bookDetail.setQuantity(orderDetail.getQuantity());
                bookDetail.setPrice(orderDetail.getPrice());
                bookDetail.setImageUrls(getImageUrlsFromBook(orderDetail.getBook()));

                // Lấy feedback của sách, chỉ trả về feedback của người dùng hiện tại
                List<Feedback> feedbacks = getFeedbacksForBooks(order.getId(),orderDetail.getBook().getIdBook(),currentUsername);
                List<FeedbackRespone> feedbackResponses = feedbacks.stream()
                        .map(feedback -> {
                            FeedbackRespone feedbackResponse = new FeedbackRespone();
                            feedbackResponse.setUsername(feedback.getUser().getUsername());
                            feedbackResponse.setComment(feedback.getComment());
                            feedbackResponse.setRating(feedback.getRating());
                            return feedbackResponse;
                        })
                        .collect(Collectors.toList());

                bookDetail.setFeedbacks(feedbackResponses);
                return bookDetail;
            })
            .collect(Collectors.toList());

    response.setBooks(bookDetails);

    // Tính tổng tiền của đơn hàng
    double totalAmount = order.getOrderDetails().stream()
            .mapToDouble(detail -> detail.getQuantity() * detail.getPrice())
            .sum();
    response.setTotal(totalAmount);

    return response;
}

//    private OrderResponse convertToOrderResponse(Order order) {
//        // Lấy username từ SecurityContextHolder
//        String currentUsername = getCurrentUsernameFromContext();
//
//        OrderResponse response = new OrderResponse();
//        response.setId(order.getId());
//        response.setShipment(order.getShipment().getShippingMethod());
//        response.setPayment(order.getPayment().getPaymentMethod());
//        response.setCreatedAt(order.getCreatedAt());
//        response.setStatus(order.getOrderStatus().getStatus());
//        response.setPhone(order.getPhone());
//        response.setShippingAdrress(order.getShippingAddress());
//        response.setReceiveName(order.getReceivingName());
//        response.setNote(order.getNote());
//        response.setDeliveryDate(order.getDeliveryDate());
//        response.setOrderCode(order.getOrderCode());
//        response.setVoucher(order.getVoucher().getDiscountValue());
//
//        List<BookRespone> bookDetails = order.getOrderDetails().stream()
//                .map(orderDetail -> {
//                    BookRespone bookDetail = new BookRespone();
//                    bookDetail.setOrderDetailId(orderDetail.getIdOrderDetail());
//                    bookDetail.setIdBook(orderDetail.getBook().getIdBook());
//                    bookDetail.setNameBook(orderDetail.getBook().getNameBook());
//                    bookDetail.setAuthor(orderDetail.getBook().getAuthor());
//                    bookDetail.setQuantity(orderDetail.getQuantity());
//                    bookDetail.setPrice(orderDetail.getPrice());
//                    bookDetail.setImageUrls(getImageUrlsFromBook(orderDetail.getBook()));
//
//                    // Lấy feedback của sách, chỉ trả về feedback của người dùng hiện tại
//                    List<Feedback> feedbacks = getFeedbacksForBook(orderDetail.getBook().getIdBook(), currentUsername);
//                    List<FeedbackRespone> feedbackResponses = feedbacks.stream()
//                            .map(feedback -> {
//                                FeedbackRespone feedbackResponse = new FeedbackRespone();
//                                feedbackResponse.setUsername(feedback.getUser().getUsername());
//                                feedbackResponse.setComment(feedback.getComment());
//                                feedbackResponse.setRating(feedback.getRating());
//                                return feedbackResponse;
//                            })
//                            .collect(Collectors.toList());
//
//                    bookDetail.setFeedbacks(feedbackResponses);
//                    return bookDetail;
//                })
//                .collect(Collectors.toList());
//
//        response.setBooks(bookDetails);
//
//        // Tính tổng tiền của đơn hàng
//        double totalAmount = order.getOrderDetails().stream()
//                .mapToDouble(detail -> detail.getQuantity() * detail.getPrice())
//                .sum();
//        response.setTotal(totalAmount);
//
//        return response;
//    }

    private String getCurrentUsernameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();  // Trả về username từ SecurityContext
        }
        return null;  // Nếu không có authentication thì trả về null
    }
    private List<Feedback> getFeedbacksForBooks(Integer orderId,Integer bookId,String username) {
      //  return feedbackRepository.findByOrderDetail_Book_IdBookOrderByCreatedAtDesc(bookId);
       // return feedbackRepository.findByBookIdAndUsername(bookId, username);
        return feedbackRepository.findByOrderDetail_Order_IdAndOrderDetail_Book_IdBookAndOrderDetail_Order_User_Username( orderId,bookId,username);
    }


    private List<String> getImageUrlsFromBook(Book book) {
        String baseUrl = "http://localhost:8080/book/image/";
        return book.getImageBooks().stream()
                .map(image -> baseUrl + encodeURIComponent(image.getImage_url()))
                .collect(Collectors.toList());
    }

    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public void cancelOrder(String token, Integer orderId) throws Exception {
        String username = jwtService.extractUsername(token); // Extract username từ token

        // Tìm người dùng dựa trên username (token đã giải mã)
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found for username: " + username));

        // Lấy userId từ đối tượng UserEntity
        Integer userId = user.getIdUser();
        Order order = orderRepository.findByIdAndUser_idUser(orderId, userId)
                .orElseThrow(() -> new Exception("Order not found"));
        OrderStatus pendingStatus;
        try {
            pendingStatus = statusRespository.findByStatus("Processing");
            if (pendingStatus == null) {
                throw new Exception("Pending status not found");
            }
        } catch (Exception e) {
            throw new Exception("Error finding canceled status: " + e.getMessage());
        }


        if (!order.getOrderStatus().equals(pendingStatus)) {
            throw new Exception("Order cannot be canceled as it is not in pending state.");
        }

        // Cập nhật trạng thái hủy đơn hàng
        OrderStatus canceledStatus;
        try {
            canceledStatus = statusRespository.findByStatus("Cancel");
            if (canceledStatus == null) {
                throw new Exception("Canceled status not found");
            }
        } catch (Exception e) {
            throw new Exception("Error finding canceled status: " + e.getMessage());
        }

        order.setOrderStatus(canceledStatus);
        orderRepository.save(order);

        // Cập nhật lại số lượng sản phẩm trong kho
        for (OrderDetail detail : order.getOrderDetails()) {
            Book product = detail.getBook();
            product.setQuantity(product.getQuantity() + detail.getQuantity());
            bookCategoryResposity.save(product);
        }
    }

public Map<String, Object> getAllOrdersWithPagination(Pageable pageable,
                                                      String orderCode,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate) {

    Page<Order> ordersPage = orderRepository.findOrdersWithSearch(orderCode, startDate, endDate, pageable);
    List<OrderResponse> orderResponses = ordersPage.getContent().stream()
               .map(this::convertToOrderResponse)
               .collect(Collectors.toList());

    Map<String, Object> response = new HashMap<>();
    response.put("orders", orderResponses);
    response.put("currentPage", ordersPage.getNumber());
    response.put("totalItems", ordersPage.getTotalElements());
    response.put("totalPages", ordersPage.getTotalPages());

    return response;
}



//    @Override
//    public String getOrderIdByOrderCode(String orderCode) {
//        return null;
//    }

    // Lấy chi tiết đơn hàng cho user
    public OrderResponse getOrderDetailForUser(Integer orderId, String token) {
        String username = jwtService.extractUsername(token); // Extract username từ token

        // Tìm người dùng dựa trên username (token đã giải mã)
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found for username: " + username));

        // Lấy userId từ đối tượng UserEntity
        Integer userId = user.getIdUser();
        Order order = orderRepository.findByIdAndUser_idUser(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return convertToOrderResponse(order);
    }





    public OrderResponse getOrderDetailForAdmin(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return convertToOrderResponse(order);
    }



    public boolean updateOrderStatus(Integer orderId, Integer statusId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Optional<OrderStatus> optionalStatus = statusRespository.findById(statusId);

        // Kiểm tra nếu đơn hàng và trạng thái tồn tại
        if (optionalOrder.isPresent() && optionalStatus.isPresent()) {
            Order order = optionalOrder.get();
            OrderStatus status = optionalStatus.get();
            if (order.getOrderStatus().getIdStatus().equals(4)) {
                return false; // Không cho phép chuyển trạng thái nếu đã là "completed"
            }
            // Cập nhật trạng thái mới
            order.setOrderStatus(status);

            // Lưu lại thay đổi vào cơ sở dữ liệu
            orderRepository.save(order);

            return true;
        }

        return false; // Nếu đơn hàng hoặc trạng thái không tồn tại
    }


    public Map<String, Object> getStatistics() {
        // Lấy tổng số đơn hàng
        long totalOrders = orderRepository.countTotalOrders();
        long totalUsers=userRepository.countTotalUser();
        long totalBook=bookCategoryResposity.countTotalsBook();
        // Lấy tổng số nhân viên


        // Tính tổng doanh thu
        Double totalRevenue = orderRepository.calculateTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue =  0.0; // Nếu không có doanh thu, trả về 0
        }
        Integer totalRevenueInt = totalRevenue.intValue();

        // Chuẩn bị dữ liệu phản hồi
        Map<String, Object> response = new HashMap<>();
        response.put("totalUsers", totalUsers);
        response.put("totalBook", totalBook);
        response.put("totalOrders", totalOrders);
        response.put("totalIcomes", totalRevenueInt);

        return response;
    }



    public Map<String, Object> getMonthlyRevenue() {

        int currentYear = LocalDateTime.now().getYear();

        // Lấy dữ liệu từ repository
        List<Object[]> revenueData = orderRepository.getMonthlyRevenue(currentYear);

        // Khởi tạo mảng dữ liệu doanh thu và mảng tháng
        List<Integer> revenueList = new ArrayList<>(Collections.nCopies(12, 0));

        // Xử lý dữ liệu để đưa vào đúng định dạng
        for (Object[] row : revenueData) {
            Integer month = (Integer) row[0];
            Double totalRevenue = (Double) row[1];
            Integer totalRevenueInt = totalRevenue.intValue();

            // Đưa doanh thu vào đúng vị trí của tháng
            revenueList.set(month - 1, totalRevenueInt);
        }


        // Chuẩn bị dữ liệu trả về
        Map<String, Object> response = new HashMap<>();

        response.put("data", revenueList);
        response.put("month", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));

        return response;
    }



}




