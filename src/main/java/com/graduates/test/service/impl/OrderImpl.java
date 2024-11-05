package com.graduates.test.service.impl;

import ch.qos.logback.core.status.Status;
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
    private PaymentResponsitory paymentRepository; // Để lấy thông tin phương thức thanh toán

    @Autowired
    private ShipmentRespository shipmentRepository; // Để lấy thông tin phương thức vận chuyển
    @Autowired
    private StatusRespository statusRespository;
    @Autowired
    private BookCategoryResposity bookCategoryResposity;

    @Autowired
    private  FeedbackRepository feedbackRepository;


    public OrderImpl(UserResposity userRepository, OrderRespository orderRepository, CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderDetailRepository orderDetailRepository, PaymentResponsitory paymentRepository, ShipmentRespository shipmentRepository, StatusRespository statusRespository, BookCategoryResposity bookCategoryResposity, FeedbackRepository feedbackRepository) {
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
    }

    @Override
    public Order createOrder(Integer userId, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId, String phone, String receivingName, String note) throws Exception {
        // Tìm giỏ hàng của người dùng

        Cart cart = cartRepository.findByUserIdUser(userId)
                .orElseThrow(() -> new Exception("Cart not found for user ID: " + userId));

        // Tìm payment và shipment từ database
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment not found with ID: " + paymentId));

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new Exception("Shipment not found with ID: " + shipmentId));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setCart(cart);
        //   order.setUser(cart.getUser()); // Lấy thông tin người dùng từ giỏ hàng
        order.setShippingAddress(shippingAddress);
        order.setPayment(payment); // Thiết lập payment
        order.setShipment(shipment); // Thiết lập shipment
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalAmount());
        order.setPhone(phone);
        order.setReceivingName(receivingName);
        //  order.setOrderStatus();
        order.setUser(cart.getUser());
        OrderStatus pendingStatus = statusRespository.findByStatus("Processing"); // Tìm trạng thái "Pending" từ bảng Status
        order.setOrderStatus(pendingStatus);
        order.setNote(note);
        order.setDeliveryDate(LocalDateTime.now().plusDays(5));
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
    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        List<Order> orders;
        orders = orderRepository.findByUser_idUserOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(this::convertToOrderResponse) // Chuyển đổi từng Order sang OrderResponse
                .collect(Collectors.toList());

    }

    @Override
    public Order getOrderById(Integer idOrder) {
        return orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + idOrder));
    }




    private OrderResponse convertToOrderResponse(Order order) {
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
                    List<Feedback> feedbacks = getFeedbacksForBook(orderDetail.getBook().getIdBook());

                    // Chuyển đổi feedback thành FeedbackResponse
                    List<FeedbackRespone> feedbackResponses = feedbacks.stream()
                            .map(feedback -> {
                                FeedbackRespone feedbackResponse = new FeedbackRespone();
                                feedbackResponse.setUsername(feedback.getUser().getUsername()); // Giả sử có phương thức getUsername() trong UserEntity
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
    private List<Feedback> getFeedbacksForBook(Integer bookId) {
        return feedbackRepository.findByOrderDetail_Book_IdBookOrderByCreatedAtDesc(bookId);
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


    public void cancelOrder(Integer userId, Integer orderId) throws Exception {
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
    public Map<String, Object> getAllOrdersWithPagination(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orderResponses);
        response.put("currentPage", orderPage.getNumber());
     //   response.put("totalItems", orderPage.getTotalElements());
        response.put("totalPages", orderPage.getTotalPages());

        return response;
    }

    // Lấy chi tiết đơn hàng cho user
    public OrderResponse getOrderDetailForUser(Integer orderId, Integer userId) {
//
//        List<Object[]> results = orderRepository.findOrderWithFeedbackByUser(orderId, userId);
//
//        if (results.isEmpty()) {
//            throw new ResourceNotFoundException("Order not found with id: " + orderId + " for user id: " + userId);
//        }
//
//        OrderResponse orderResponse = null;
//        List<FeedbackRespone> feedbackResponses = new ArrayList<>();
//
//        for (Object[] row : results) {
//            if (orderResponse == null) {
//                orderResponse = new OrderResponse(
//                        (Integer) row[0],         // id
//                        (String) row[1],          // orderCode
//                        (Integer) row[2],         // bookId
//                        (String) row[3],          // title
//                        (String) row[4],          // author
//                        (String) row[5],          // description
//                        (int) row[6],             // quantity
//                        (double) row[7],          // price
//                        (List<String>) row[8],    // imageUrls
//                        (double) row[9],          // total
//                        (String) row[10],         // shipment
//                        (String) row[11],         // payment
//                        (String) row[12],         // phone
//                        (String) row[13],         // shippingAddress
//                        (String) row[14],         // receiveName
//                        (LocalDateTime) row[15],  // date
//                        (String) row[16],         // status
//                        (String) row[17],         // note
//                        (LocalDateTime) row[18],  // deliveryDate
//                        (List<BookRespone>) row[19],  // books
//                        (LocalDateTime) row[20],  // createdAt
//                        feedbackResponses
//                );
//            }
//
//            Integer feedbackId = (Integer) row[21];
//            if (feedbackId != null) {
//                FeedbackRespone feedbackResponse = new FeedbackRespone(
//                        feedbackId,
//                        (Integer) row[22],              // userId
//                        (String) row[23],               // username
//                        (Integer) row[24],              // orderDetailId
//                        (Integer) row[25],              // bookId
//                        (String) row[26],               // bookTitle
//                        (String) row[27],               // comment
//                        (Integer) row[28],              // rating
//                        (LocalDateTime) row[29],        // createdAt
//                        (LocalDateTime) row[30]         // updateAt
//                );
//                feedbackResponses.add(feedbackResponse);
//            }
//        }
//
//        return orderResponse;
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

//    public OrderResponse getOrderWithDetailsAndFeedbacks(Integer orderId) {
//        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
//
//        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
//                .map(orderDetail -> {
//                    OrderDetailResponse response = new OrderDetailResponse();
//                    response.setIdOrderDetail(orderDetail.getIdOrderDetail());
//                    response.set(orderDetail.getProductName());
//                    response.setQuantity(orderDetail.getQuantity());
//                    response.set(orderDetail.getPrice());
//                    return response;
//                }).collect(Collectors.toList());
//
//        List<FeedbackRespone> feedbackResponses = feedbackRepository.findByOrderDetailOrderId(orderId).stream()
//                .map(feedback -> {
//                    FeedbackRespone response = new FeedbackRespone();
//                    response.setIdFeedback(feedback.getIdFeedback());
//                  //  response.setUserName(feedback.getUser().getUsername());
//                    response.setComment(feedback.getComment());
//                    response.setRating(feedback.getRating());
//                    response.setCreatedAt(feedback.getCreatedAt());
//                    return response;
//                }).collect(Collectors.toList());
//
//        OrderResponse orderResponse = new OrderResponse();
//        orderResponse.setIdOrder(order.getIdOrder());
//        orderResponse.setUserName(order.getUser().getUsername());
//        orderResponse.setOrderDetails(orderDetailResponses);
//        orderResponse.setFeedbacks(feedbackResponses);
//
//        return orderResponse;
//    }


}




