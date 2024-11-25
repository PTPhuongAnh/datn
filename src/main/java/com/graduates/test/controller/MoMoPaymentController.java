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
  //  @CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép từ frontend này
    @PostMapping("/momo")
    public ResponseEntity<String> initiatePayment(@RequestParam Integer idorder,
                                                  @RequestParam String amount,
                                                  @RequestParam String orderInfo,
                                                  @RequestParam String email) {
        try {

            String response = paymentService.initiatePayment(idorder, amount, orderInfo, email);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment initiation failed: " + e.getMessage());
        }
    }
    @PostMapping("/atm")
    public ResponseEntity<String> PaymentATM(@RequestParam Integer idorder,
                                                  @RequestParam String amount,
                                                  @RequestParam String email) {
        try {

            String response = paymentService.initiateATMRequest(idorder, amount, email);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment initiation failed: " + e.getMessage());
        }
    }

}
