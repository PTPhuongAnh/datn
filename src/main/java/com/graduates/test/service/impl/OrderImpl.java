package com.graduates.test.service.impl;

import ch.qos.logback.core.status.Status;
import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.CategorySalesDTO;
import com.graduates.test.dto.OrderResponse;
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


    public OrderImpl(UserResposity userRepository, OrderRespository orderRepository, CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderDetailRepository orderDetailRepository, PaymentResponsitory paymentRepository, ShipmentRespository shipmentRepository, StatusRespository statusRespository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentRepository = paymentRepository;
        this.shipmentRepository = shipmentRepository;
        this.statusRespository = statusRespository;
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

        // Thiết lập chi tiết đơn hàng
        for (Integer detailId : selectedCartDetailIds) {
            //        CartDetail cartDetail = cartDetailRepository.findById(detailId)
            //              .orElseThrow(() -> new Exception("Cart detail not found with ID: " + detailId));

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

    @Override
    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        List<Order> orders;

            orders = orderRepository.findByUser_idUser(userId);

        return orders.stream()
                .flatMap(order -> order.getOrderDetails().stream()
                        .map(this::convertToOrderResponse)) // Chuyển đổi từng CartDetail sang CartResponse
                .collect(Collectors.toList());
    }
    private OrderResponse convertToOrderResponse(OrderDetail orderDetail) {
        Book book = orderDetail.getBook();
        List<String> imageUrls = getImageUrlsFromBook(book);
        Order order = orderDetail.getOrder();
        Shipment shipment = order.getShipment();
        Payment payment = order.getPayment();
        OrderStatus status = order.getOrderStatus();
        return new OrderResponse(
                order.getId(),
                book.getIdBook(),
                book.getNameBook(),
                book.getAuthor(),
                book.getDescription_short(),
                orderDetail.getQuantity(),
                orderDetail.getPrice(),
                imageUrls,
                order.getTotalAmount(),
                shipment.getShippingMethod(),
                payment.getPaymentMethod(),
                order.getPhone(),
                order.getShippingAddress(),
                order.getReceivingName(),
                order.getCreatedAt(),
                status.getStatus(),
                order.getNote(),
                order.getDeliveryDate()
        );
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
            pendingStatus = statusRespository.findByStatus("Pending");
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


    public Page<OrderResponse> getAllOrdersForAdmin(Pageable pageable) {
        // return orderRepository.findAll(pageable);
        Page<Order> orders = orderRepository.findAll(pageable);

        // Chuyển mỗi đối tượng Order thành OrderResponse
        List<OrderResponse> orderResponses = orders.stream()
                .flatMap(order -> order.getOrderDetails().stream()
                        .map(this::convertToOrderResponse)) // Chuyển đổi từng OrderDetail sang OrderResponse
                .collect(Collectors.toList());

        // Trả về PageImpl để chuyển List thành Page
        return new PageImpl<>(orderResponses, pageable, orders.getTotalElements());
    }
    public OrderResponse getOrderDetails(Integer orderId) {
        // Tìm đơn hàng theo orderId
        OrderDetail order = orderDetailRepository.findById(orderId).orElse(null);

        if (order == null) {
            return null; // Nếu đơn hàng không tồn tại
        }

        // Chuyển đổi Order thành OrderResponse (bao gồm các thông tin chi tiết)
        return convertToOrderResponse(order);
    }

    public boolean updateOrderStatus(Integer orderId, Integer statusId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Optional<OrderStatus> optionalStatus = statusRespository.findById(statusId);

        // Kiểm tra nếu đơn hàng và trạng thái tồn tại
        if (optionalOrder.isPresent() && optionalStatus.isPresent()) {
            Order order = optionalOrder.get();
            OrderStatus status = optionalStatus.get();

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

        // Lấy tổng số nhân viên


        // Tính tổng doanh thu
        Double totalRevenue = orderRepository.calculateTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue =  0.0; // Nếu không có doanh thu, trả về 0
        }
        Integer totalRevenueInt = totalRevenue.intValue();

        // Chuẩn bị dữ liệu phản hồi
        Map<String, Object> response = new HashMap<>();
        response.put("totalOrders", totalOrders);
        response.put("totalRevenueInt", totalRevenueInt);

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




