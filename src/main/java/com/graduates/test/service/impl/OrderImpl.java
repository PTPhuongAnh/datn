package com.graduates.test.service.impl;

import com.graduates.test.model.*;
import com.graduates.test.resposity.*;
import com.graduates.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    public OrderImpl(UserResposity userRepository, OrderRespository orderRepository, CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderDetailRepository orderDetailRepository, PaymentResponsitory paymentRepository, ShipmentRespository shipmentRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentRepository = paymentRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public Order createOrder(Integer userId, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId) throws Exception {
        // Tìm giỏ hàng của người dùng
//
        Cart cart =  cartRepository.findByUserIdUser(userId)
                .orElseThrow(() -> new Exception("Cart not found for user ID: " + userId));

        // Tìm payment và shipment từ database
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment not found with ID: " + paymentId));

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new Exception("Shipment not found with ID: " + shipmentId));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(cart.getUser()); // Lấy thông tin người dùng từ giỏ hàng
        order.setShippingAddress(shippingAddress);
        order.setPayment(payment); // Thiết lập payment
        order.setShipment(shipment); // Thiết lập shipment
        order.setCreatedAt(LocalDateTime.now());

        // Thiết lập chi tiết đơn hàng
        for (Integer detailId : selectedCartDetailIds) {
            CartDetail cartDetail = cartDetailRepository.findById(detailId)
                    .orElseThrow(() -> new Exception("Cart detail not found with ID: " + detailId));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(cartDetail.getBook());
            orderDetail.setQuantity(cartDetail.getQuantity());
            order.addOrderDetail(orderDetail); // Thêm chi tiết đơn hàng vào đơn hàng
        }

        // Lưu đơn hàng vào database
        return orderRepository.save(order);
    }

}


