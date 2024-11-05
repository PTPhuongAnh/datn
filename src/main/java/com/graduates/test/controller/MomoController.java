package com.graduates.test.controller;

import com.graduates.test.dto.MomoResponse;
import com.graduates.test.service.MomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/momo")
public class MomoController {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.redirectUrl}")
    private String redirectUrl;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    private static final String HMAC_SHA256 = "HmacSHA256";

    @PostMapping("/create-payments")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> paymentData) {
        try {
            String orderInfo = "thanh toán momo";
            Integer amount = 5000;
            String orderId = String.valueOf(System.currentTimeMillis());
            String requestId = String.valueOf(System.currentTimeMillis());
            String requestType = "payWithATM";
            String extraData = (String) paymentData.getOrDefault("extraData", "");

            // Tạo rawHash để ký HMAC
            String rawHash = String.format("accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                    accessKey, amount, extraData, ipnUrl, orderId, orderInfo, partnerCode, redirectUrl, requestId, requestType);

            // Ký HMAC SHA256
            String signature = signHmacSHA256(rawHash, secretKey);

            // Chuẩn bị dữ liệu yêu cầu
            Map<String, Object> body = new HashMap<>();
            body.put("partnerCode", partnerCode);
            body.put("partnerName", "Test");
            body.put("storeId", "MomoTestStore");
            body.put("requestId", requestId);
            body.put("amount", amount);
            body.put("orderId", orderId);
            body.put("orderInfo", orderInfo);
            body.put("redirectUrl", redirectUrl);
            body.put("ipnUrl", ipnUrl);
            body.put("lang", "vi");
            body.put("extraData", extraData);
            body.put("requestType", requestType);
            body.put("signature", signature);

            // Gửi yêu cầu tới MoMo API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, body, Map.class);

            // Trả về URL thanh toán từ MoMo
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                return ResponseEntity.ok(responseBody);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create payment");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
        private String signHmacSHA256(String data, String key) throws Exception {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(data.getBytes());
            StringBuilder hash = new StringBuilder();
            for (byte b : hmacData) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        }
    }
