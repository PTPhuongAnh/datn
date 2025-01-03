package com.graduates.test.service.impl;

import com.graduates.test.model.Order;
import com.graduates.test.model.PaymentStatusM;
import com.graduates.test.resposity.OrderRespository;
import com.graduates.test.resposity.PaymentStatusMRepository;
import com.graduates.test.service.PaymentStatusMService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PaymentStatusMImpl implements PaymentStatusMService {
    @Autowired
    private OrderRespository orderRepository;

    @Autowired
    private PaymentStatusMRepository statusPaymentRepository;

    public void updateOrderPaymentStatus(String orderCode, boolean isPaid) {
        // Tìm đơn hàng theo orderCode
        Order order = orderRepository.findByOrderCode(orderCode);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        // Lấy trạng thái thanh toán tương ứng
        String statusName = isPaid ? "PAID" : "UNPAID";

        // Tìm trạng thái thanh toán
    PaymentStatusM  statusPayment = statusPaymentRepository.findByStatusName(statusName);
        if (statusPayment == null) {
            throw new RuntimeException("Status not found");
        }

        // Cập nhật trạng thái thanh toán cho đơn hàng
        order.setPaymentStatusM(statusPayment);

        // Lưu lại đơn hàng sau khi cập nhật
        orderRepository.save(order);

        // Optional: In ra thông báo
        System.out.println("Updated payment status for order " + orderCode + " to " + statusName);
    }
}
