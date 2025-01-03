package com.graduates.test.controller;

import com.graduates.test.service.PaymentStatusMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/callback")
public class PaymentStatusMController {
    @Autowired
    private PaymentStatusMService paymentService;
//    @PostMapping
//    public ResponseEntity<String> handleVnpayCallback(@RequestParam Map<String, String> params) {
//        // Kiểm tra mã phản hồi của VNPAY
//        String vnpResponseCode = params.get("vnp_ResponseCode");
//
//        // Kiểm tra nếu thanh toán thành công (vnp_ResponseCode = 00)
//        boolean isPaid = "00".equals(vnpResponseCode);
//
//        // Lấy orderCode từ tham số trả về của VNPAY
//        String orderCode = params.get("vnp_TxnRef");
//
//        // Cập nhật trạng thái thanh toán của đơn hàng
//        paymentService.updateOrderPaymentStatus(orderCode, isPaid);
//
//        // Trả về thông báo thành công
//        return ResponseEntity.ok("Callback received and order status updated");
//    }

}
