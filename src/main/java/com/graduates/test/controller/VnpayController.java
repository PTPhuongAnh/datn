package com.graduates.test.controller;

import com.graduates.test.dto.PaymentDTO;
import com.graduates.test.model.VnpayRequest;
import com.graduates.test.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VnpayController {
//    private final VnpayService vnpayService;
//
//    public VnpayController(VnpayService vnpayService) {
//        this.vnpayService = vnpayService;
//    }
//
//    @PostMapping("/create-payment")
//    public String createPayment(@RequestBody VnpayRequest request) {
//        try {
//            return vnpayService.createPaymentUrl(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error creating payment URL";
//        }
//    }
private final VnpayService paymentService;

    @Autowired
    public VnpayController(VnpayService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * API để tạo thanh toán qua VNPAY
     */
    @PostMapping("/vn-pay")
    public ResponseEntity<PaymentDTO.VNPayResponse> createVnPayPayment(HttpServletRequest request) throws Exception {
        PaymentDTO.VNPayResponse response = paymentService.createVnPayPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * API callback để xử lý kết quả trả về từ VNPAY
     */
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<String> vnPayCallback(HttpServletRequest request) {
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");

        if ("00".equals(vnpResponseCode)) {
            // Thanh toán thành công
            return ResponseEntity.ok("Payment success! Transaction reference: " + vnpTxnRef);
        } else {
            // Thanh toán thất bại hoặc bị hủy
            return ResponseEntity.status(400).body("Payment failed! Response code: " + vnpResponseCode);
        }
    }
}
