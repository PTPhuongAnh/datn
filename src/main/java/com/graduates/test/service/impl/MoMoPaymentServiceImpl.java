package com.graduates.test.service.impl;

import com.graduates.test.model.MoMoPaymentRequest;
import com.graduates.test.model.Order;
import com.graduates.test.service.MoMoPaymentService;
import com.graduates.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class MoMoPaymentServiceImpl implements MoMoPaymentService {
    @Autowired
    private OrderService orderService;
    @Value("${momo.partnerCode}")
    private String partnerCode;
    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    @Value("${momo.returnUrl}")
    private String returnUrl;

    @Value("${momo.endpoint}")
    private String endpoint;
    private final RestTemplate restTemplate;
    public MoMoPaymentServiceImpl(OrderService orderService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }
    @Override
    public String initiatePayment(Integer idorder,String amount, String orderInfo, String email) throws Exception {
        Order order =orderService.getOrderById(idorder) ;
        String orderId = order.getOrderCode();
        String requestId = orderId;

        MoMoPaymentRequest request = new MoMoPaymentRequest();
        request.setPartnerCode(partnerCode);
        request.setAccessKey(accessKey);
        request.setRequestType("captureMoMoWallet");
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);
        request.setOrderId(orderId);
        request.setAmount(amount);
        request.setOrderInfo(orderInfo);
        request.setRequestId(requestId);
        request.setExtraData(email);
        request.setLang("vi");

        // Xây dựng rawSignature và đảm bảo không có khoảng trắng không mong muốn
        String rawSignature = "partnerCode=" + request.getPartnerCode() +
                "&accessKey=" + request.getAccessKey() +
                "&requestId=" + request.getRequestId() +
                "&amount=" + request.getAmount() +
                "&orderId=" + request.getOrderId() +
                "&orderInfo=" + request.getOrderInfo() +
                "&returnUrl=" + notifyUrl+
                "&notifyUrl=" + returnUrl+
                "&extraData=" + request.getExtraData();

        // Log rawSignature để kiểm tra
        System.out.println("Raw Signature: " + rawSignature);

        // Tạo chữ ký
      String signature = generateSignature(rawSignature, secretKey);

        request.setSignature(signature);

        // Log chữ ký đã tạo
        System.out.println("Generated Signature: " + signature);

        // Gửi yêu cầu đến MoMo API
        String response = restTemplate.postForObject(endpoint, request, String.class);

        // Log phản hồi của MoMo
        System.out.println("MoMo API Response: " + response);

        return response;
    }
    @Override
    public String initiateATMRequest(Integer idorder, String amount, String email) throws Exception {
        // Tạo requestId duy nhất
      //  String requestId = UUID.randomUUID().toString();
        Order order =orderService.getOrderById(idorder) ;
        String orderId = order.getOrderCode();
        String requestId = orderId;

        // Cấu hình các tham số chính
        MoMoPaymentRequest request = new MoMoPaymentRequest();
        request.setPartnerCode(partnerCode); // MOMO_ATM_DEV
        request.setAccessKey(accessKey);     // SvDmj2cOTYZmQQ3H
        request.setRequestId(requestId);    // requestId duy nhất
        request.setAmount(amount);          // Số tiền cần thanh toán
        request.setOrderId(orderId);        // Mã đơn hàng
        request.setNotifyUrl(notifyUrl);       // Đường dẫn IPN
        request.setReturnUrl(returnUrl);  // Đường dẫn Redirect
        request.setExtraData(email);  // Email khách hàng
        request.setRequestType("payWithATM"); // Loại thanh toán


        // Tạo rawSignature
        String rawSignature = "accessKey=" + request.getAccessKey() +
                "&amount=" + request.getAmount() +
                "&extraData=" +request.getExtraData()+
                "&ipnUrl=" + request.getNotifyUrl() +
                "&orderId=" + request.getOrderId() +
                "&orderInfo=" +"test" +
                "&partnerClientId=" + request.getExtraData() +

                "&partnerCode=" + request.getPartnerCode() +
                "&redirectUrl=" + request.getReturnUrl() +
                "&requestId=" + request.getRequestId() +
                "&requestType=" + request.getRequestType();
        System.out.println("Raw Signature: " + rawSignature);

        // Tạo chữ ký
        String signature = generateSignature(rawSignature, secretKey);
        request.setSignature(signature);
        System.out.println(signature);

        // Gửi request qua RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(endpoint, request, String.class);

        System.out.println("MoMo Response: " + response);
        return response;
    }

    /**
     * Tạo chữ ký HMAC-SHA256 cho yêu cầu
     * @param data Dữ liệu cần ký
     * @param key Khóa bí mật
     * @return Chữ ký đã được mã hóa Base64
     * @throws Exception Nếu có lỗi khi tạo chữ ký
     */
    private String generateSignature(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        // Khởi tạo SecretKeySpec với khóa bí mật
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        // Thiết lập HMAC với khóa bí mật
        sha256_HMAC.init(secretKeySpec);

        // Tạo chữ ký từ dữ liệu
        byte[] hmacData = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Chuyển đổi byte[] thành chuỗi Hex
        StringBuilder hexString = new StringBuilder();
        for (byte b : hmacData) {
            hexString.append(String.format("%02x", b));
        }

        // Trả về chữ ký dưới dạng hex
        return hexString.toString();
    }
}
