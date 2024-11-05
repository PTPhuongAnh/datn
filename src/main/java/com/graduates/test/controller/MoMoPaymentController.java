package com.graduates.test.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class MoMoPaymentController {
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(MoMoPaymentController.class);
//
//    @Autowired
//    private MomoPaymentService paymentService;
//
//    @PostMapping("/create-payment")
//    public ResponseEntity<String> createPayment() {
//        logger.info("Bắt đầu tạo yêu cầu thanh toán");
//
//        String accessKey = "F8BBA842ECF85";
//        String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
//        String orderInfo = "pay with MoMo";
//        String partnerCode = "MOMO";
//        String redirectUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
//        String ipnUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
//        String requestType = "payWithMethod";
//        String amount = "50000";
//        String orderId = partnerCode + System.currentTimeMillis();
//        String requestId = orderId;
//        String extraData = "";
//        boolean autoCapture = true;
//        String lang = "vi";
//        String orderGroupId = "";
//
//        String rawSignature = String.format(
//                "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
//                accessKey, amount, extraData, ipnUrl, orderId, orderInfo, partnerCode, redirectUrl, requestId, requestType
//        );
//
//        logger.info("Chuỗi rawSignature: " + rawSignature);
//
//        try {
//            // Tạo chữ ký
//            String signature = paymentService.generateSignature(rawSignature, secretKey);
//            logger.info("Chữ ký được tạo: " + signature);
//
//            // Tạo yêu cầu thanh toán
//            MoMoPaymentRequest paymentRequest = new MoMoPaymentRequest();
//            paymentRequest.setPartnerCode(partnerCode);
//            paymentRequest.setPartnerName("Test");
//            paymentRequest.setStoreId("MomoTestStore");
//            paymentRequest.setRequestId(requestId);
//            paymentRequest.setAmount(amount);
//            paymentRequest.setOrderId(orderId);
//            paymentRequest.setOrderInfo(orderInfo);
//            paymentRequest.setRedirectUrl(redirectUrl);
//            paymentRequest.setIpnUrl(ipnUrl);
//            paymentRequest.setLang(lang);
//            paymentRequest.setRequestType(requestType);
//            paymentRequest.setAutoCapture(autoCapture);
//            paymentRequest.setExtraData(extraData);
//            paymentRequest.setOrderGroupId(orderGroupId);
//            paymentRequest.setSignature(signature);
//
//            logger.info("Đối tượng MoMoPaymentRequest đã sẵn sàng: " + paymentRequest);
//
//            // Gửi yêu cầu HTTP đến MoMo
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> response = restTemplate.postForEntity(
//                    "https://test-payment.momo.vn/v2/gateway/api/create",
//                    paymentRequest, String.class
//            );
//
//            logger.info("Phản hồi từ MoMo: " + response.getBody());
//
//            // Trả về kết quả
//            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
//
//        } catch (Exception e) {
//          // logger.error("Lỗi xử lý thanh toán", e);
//            return ResponseEntity.status(500).body("Lỗi xử lý thanh toán: " + e.getMessage());
//        }
//    }
}
