package com.graduates.test.controller;

import com.graduates.test.service.MoMoPaymentService;
import com.graduates.test.service.OrderService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/payment")
public class MoMoPaymentController {
    private final MoMoPaymentService paymentService;
    private OrderService orderService;

    @Autowired
    public MoMoPaymentController(MoMoPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/momo")
    public ResponseEntity<String> initiatePayment(@RequestParam Integer idorder,
                                                  @RequestParam String amount,
                                                  @RequestParam String orderInfo,
                                                  @RequestParam String email) {
        try {
            // Lấy orderId từ orderCode bằng OrderService
           // Integer orderId = orderService.getOrderIdByOrderCode(orderCode);

            // Gọi PaymentService để thực hiện thanh toán với orderId
            String response = paymentService.initiatePayment(idorder, amount, orderInfo, email);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment initiation failed: " + e.getMessage());
        }
    }

}
