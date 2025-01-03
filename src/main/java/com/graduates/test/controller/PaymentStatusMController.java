package com.graduates.test.controller;

import com.graduates.test.service.PaymentStatusMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("vnpay/callback")
public class PaymentStatusMController {
    @Autowired
    private PaymentStatusMService paymentService;

    @PostMapping
    public ResponseEntity<String> handleVnpayCallback(@RequestParam("vnp_ResponseCode") String vnpResponseCode,
                                                      @RequestParam("vnp_TxnRef") String orderCode,
                                                      @RequestParam("vnp_Amount") String amount,
                                                      @RequestParam("vnp_OrderInfo") String bankCode,
                                                      @RequestParam("vnp_SecureHash") String transactionNo) {
        System.out.println("Received params: vnp_ResponseCode=" + vnpResponseCode + ", orderCode=" + orderCode);

        boolean isPaid = "00".equals(vnpResponseCode);

        try {
            paymentService.updateOrderPaymentStatus(orderCode, isPaid);
            return ResponseEntity.ok("Callback received and order status updated");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found");
        }
//    public ResponseEntity<String> handleVnpayCallback(@RequestParam Map<String, String> params) {
//        System.out.println("Callback received: " + params);
//        // Kiểm tra mã phản hồi của VNPAY
//        String vnpResponseCode = params.get("vnp_ResponseCode");
//
//        // Kiểm tra nếu thanh toán thành công (vnp_ResponseCode = 00)
//        boolean isPaid = "00".equals(vnpResponseCode);
//
//        // Lấy orderCode từ tham số trả về của VNPAY
//
//        String orderCode = params.get("vnp_TxnRef");
//        System.out.println("Order code from VNPAY: " + orderCode);
//
//        // Cập nhật trạng thái thanh toán của đơn hàng
//        paymentService.updateOrderPaymentStatus(orderCode, isPaid);
//
//        // Trả về thông báo thành công
//        return ResponseEntity.ok("Callback received and order status updated");
//    }

    }
}
