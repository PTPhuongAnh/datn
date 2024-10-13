package com.graduates.test.service.impl;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.*;
import com.graduates.test.resposity.*;
import com.graduates.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderImpl implements OrderService {
    @Autowired
    private UserResposity userRepository;

    @Autowired
    private OrderRespository orderRepository;

    @Autowired
    private CartRepository cartRepository;
    private  CartDetailRepository cartDetailRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private PaymentResponsitory paymentRepository; // Để lấy thông tin phương thức thanh toán

    @Autowired
    private ShipmentRespository shipmentRepository; // Để lấy thông tin phương thức vận chuyển
    @Autowired
    private  StatusRespository statusRespository;
    @Autowired
    private  BookCategoryResposity bookCategoryResposity;


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
    public Order createOrder(Integer userId, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId,String phone, String receivingName) throws Exception {
        // Tìm giỏ hàng của người dùng

        Cart cart =  cartRepository.findByUserIdUser(userId)
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
        OrderStatus pendingStatus = statusRespository.findByStatus("Pending"); // Tìm trạng thái "Pending" từ bảng Status
        order.setOrderStatus(pendingStatus);

        // Thiết lập chi tiết đơn hàng
        for (Integer detailId : selectedCartDetailIds) {
    //        CartDetail cartDetail = cartDetailRepository.findById(detailId)
      //              .orElseThrow(() -> new Exception("Cart detail not found with ID: " + detailId));

               CartDetail cartDetail = cartDetailRepository.findByIdAndCart_IdCart(detailId, cart.getIdCart())

                       .orElseThrow(() ->new Exception("Cart detail not found with ID: " + detailId + " for cart ID: " + cart.getIdCart()));

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
    public List<OrderResponse> getOrdersByUserIdAndOptionalStatus(Integer userId, Integer statusId) {
        List<Order> orders;
        if (statusId != null) {
            orders = orderRepository.findByUser_IdUserAndOrderStatus_IdStatus(userId, statusId);
        } else {
            orders = orderRepository.findByUser_idUser(userId);
        }
        return orders.stream()
                .flatMap(order -> order.getOrderDetails().stream()
                        .map(this::convertToOrderResponse)) // Chuyển đổi từng CartDetail sang CartResponse
                .collect(Collectors.toList());
    }




    private OrderResponse convertToOrderResponse(OrderDetail orderDetail) {
        Book book = orderDetail.getBook();
        List<String> imageUrls = getImageUrlsFromBook(book);
        Order order=orderDetail.getOrder();
        Shipment shipment = order.getShipment();
        Payment payment = order.getPayment();
        OrderStatus status=order.getOrderStatus();
        return new OrderResponse(
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
                status.getStatus()



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
        // Tìm đơn hàng theo ID và kiểm tra xem nó có thuộc về user không
        Order order = orderRepository.findByIdAndUser_idUser(orderId, userId)
                .orElseThrow(() -> new Exception("Order not found"));
        OrderStatus pendingStatus;
        try {
            pendingStatus=statusRespository.findByStatus("Pending");
            if(pendingStatus==null){
                throw new Exception("Pending status not found");
            }
        }catch (Exception e){
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
}




