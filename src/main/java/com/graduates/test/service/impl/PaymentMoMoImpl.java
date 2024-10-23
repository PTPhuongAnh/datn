package com.graduates.test.service.impl;

import com.graduates.test.model.Order;
import com.graduates.test.model.Payment;
import com.graduates.test.model.Transaction;
import com.graduates.test.resposity.OrderRespository;
import com.graduates.test.resposity.TransactionRepository;
import com.graduates.test.service.MomoPaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentMoMoImpl implements MomoPaymentService {
//    @Value("${momo.payment_url}")
//    private String momoPaymentUrl;
//
//    @Value("${momo.partner_code}")
//    private String partnerCode;
//
//    @Value("${momo.access_key}")
//    private String accessKey;
//
//    @Value("${momo.secret_key}")
//    private String secretKey;
//
//    @Value("${momo.order_info}")
//    private String orderInfo;
//
//    @Value("${momo.redirect_url}")
//    private String redirectUrl;
//
//    @Value("${momo.notify_url}")
//    private String notifyUrl;
//
    private final RestTemplate restTemplate;
    private final TransactionRepository transactionRepository;
    private final OrderRespository orderRepository;


    public PaymentMoMoImpl(RestTemplate restTemplate, TransactionRepository transactionRepository, OrderRespository orderRepository) {
        this.restTemplate = restTemplate;
        this.transactionRepository = transactionRepository;
        this.orderRepository = orderRepository;
    }
//
private String partnerCode = "MOMO5ZIJ20230613";
    private String accessKey = "bAbusuVpcdguvDX7";
    private String secretKey = "w6HsnH2Yu0UTDSfNo1ZxVZkPTESottzM";
    private String orderInfo = "Thanh toán đơn hàng Momo";
    private String redirectUrl = "https://sangle.free.beeceptor.com";
    private String notifyUrl = "https://sangle.free.beeceptor.com";
    private String momoPaymentUrl = "https://test-payment.momo.vn/v2/gateway/api/create"; // API MoMo thanh toán

    // Phương thức tạo thanh toán MoMo và in ra chữ ký
    public void createMomoPayment() {
        // Hardcode một số giá trị
        String orderId ="12"; // Tạo orderId duy nhất
        Integer amount = 10000;  // Số tiền cố định để test

        // Tạo body của request (chỉ cần sử dụng để tạo chữ ký)
        Map<String, Object> body = new HashMap<>();
        body.put("partnerCode", partnerCode);
        body.put("accessKey", accessKey);
        body.put("requestId", orderId);  // requestId giống orderId để test
        body.put("amount", amount);  // Số tiền cố định
        body.put("orderId", orderId);  // orderId cố định
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", redirectUrl);
        body.put("ipnUrl", notifyUrl);  // Đường dẫn IPN (notifyUrl)
        body.put("extraData", "");
        body.put("requestType", "paymentCode");

        // Tạo chữ ký từ body
        String signature = generateSignature(body);

        // In ra chữ ký
        System.out.println("Chữ ký: " + signature);

        // Thêm chữ ký vào body
        body.put("signature", "/TIyBQ+bs0jR8Me/fNJLzxbmk53a2CZt1hahHTGN/gk=");

        // Gửi yêu cầu tới MoMo
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(momoPaymentUrl, HttpMethod.POST, request, Map.class);

        // Lấy URL thanh toán từ phản hồi
        if (response.getBody() != null) {
            String paymentUrl = (String) response.getBody().get("payUrl");
            System.out.println("URL thanh toán MoMo: " + paymentUrl);
        } else {
            System.out.println("Lỗi trong phản hồi từ MoMo: " + response.getStatusCode());
        }
    }

    // Phương thức tạo chữ ký HMAC-SHA256
    private String generateSignature(Map<String, Object> body) {
        try {
            StringBuilder signatureData = new StringBuilder();
            signatureData.append("partnerCode=").append(body.get("partnerCode"))
                    .append("&accessKey=").append(body.get("accessKey"))
                    .append("&requestId=").append(body.get("requestId"))
                    .append("&amount=").append(body.get("amount"))
                    .append("&orderId=").append(body.get("orderId"))
                    .append("&orderInfo=").append(body.get("orderInfo"))
                    .append("&redirectUrl=").append(body.get("redirectUrl"))
                    .append("&ipnUrl=").append(body.get("ipnUrl"))
                    .append("&extraData=").append(body.get("extraData"))
                    .append("&requestType=").append(body.get("requestType"))
                    .append("&secretKey=").append("w6HsnH2Yu0UTDSfNo1ZxVZkPTESottzM");  // Cố định secretKey

            // Tạo chữ ký HMAC-SHA256
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(signatureData.toString().getBytes());

            return Base64.getEncoder().encodeToString(rawHmac);  // Chuyển kết quả thành Base64
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký", e);
        }
    }

}
