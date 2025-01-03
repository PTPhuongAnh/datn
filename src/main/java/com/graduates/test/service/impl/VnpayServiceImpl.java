package com.graduates.test.service.impl;

import com.graduates.test.Config.VNPayUtil;
import com.graduates.test.Config.VnpayConfig;
import com.graduates.test.dto.PaymentDTO;
import com.graduates.test.model.OrderStatus;
import com.graduates.test.model.PaymentStatusM;
import com.graduates.test.model.VnpayRequest;
import com.graduates.test.resposity.PaymentStatusMRepository;
import com.graduates.test.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class VnpayServiceImpl implements VnpayService {
    @Override
    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) throws Exception {
        return null;
    }
    @Autowired
   private PaymentStatusMRepository  paymentStatusMRepository;
//    private final VnpayConfig vnpayConfig;
//
//    public VnpayServiceImpl(VnpayConfig vnpayConfig) {
//        this.vnpayConfig = vnpayConfig;
//    }
//
//    @Override
//    public String createPaymentUrl(VnpayRequest request) throws Exception {
//        String vnpVersion = "2.1.0";
//        String vnpCommand = "pay";
//
//      //  String vnpOrderInfo = request.getOrderInfo();
//        String orderType = "other";
//        String vnpTxnRef = request.getOrderId(); // Mã đơn hàng
//        String vnpIpAddr = "127.0.0.1";          // Địa chỉ IP của người dùng
//
//        // Tạo thời gian giao dịch
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnpCreateDate = formatter.format(calendar.getTime());
//
//        // Cộng thêm 15 phút cho expired time
//        calendar.add(Calendar.MINUTE, 15);
//        String vnpExpireDate = formatter.format(calendar.getTime());
//
//        Map<String, String> vnpParams = new HashMap<>();
//        vnpParams.put("vnp_Version", vnpVersion);
//        vnpParams.put("vnp_Command", vnpCommand);
//        vnpParams.put("vnp_TmnCode", vnpayConfig.getVnpTmnCode());
//        vnpParams.put("vnp_Amount", String.valueOf(Integer.parseInt(request.getAmount()) * 100));
//        vnpParams.put("vnp_CurrCode", "VND");
//        vnpParams.put("vnp_TxnRef", vnpTxnRef);
//        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang:");
//        vnpParams.put("vnp_OrderType", orderType);
//        vnpParams.put("vnp_ReturnUrl", vnpayConfig.getVnpReturnUrl());
//        vnpParams.put("vnp_IpAddr", vnpIpAddr);
//        vnpParams.put("vnp_CreateDate", vnpCreateDate);
//        vnpParams.put("vnp_ExpireDate", vnpExpireDate);
//
//        if (request.getBankCode() != null && !request.getBankCode().isEmpty()) {
//            vnpParams.put("vnp_BankCode", request.getBankCode());
//        }
//
//        // Tạo URL thanh toán
//        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        for (String fieldName : fieldNames) {
//            String value = vnpParams.get(fieldName);
//            if (value != null && !value.isEmpty()) {
//                hashData.append(fieldName).append('=').append(URLEncoder.encode(value, "UTF-8"));
//                query.append(URLEncoder.encode(fieldName, "UTF-8"))
//                        .append('=')
//                        .append(URLEncoder.encode(value, "UTF-8"));
//                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
//                    hashData.append('&');
//                    query.append('&');
//                }
//            }
//        }
//
//        String secureHash = hmacSHA512(vnpayConfig.getVnpHashSecret(), hashData.toString());
//        query.append("&vnp_SecureHash=").append(secureHash);
//
//        return vnpayConfig.getVnpPayUrl() + "?" + query.toString();
//    }
//
//    private String hmacSHA512(String key, String data) throws Exception {
//        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
//        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA512");
//        mac.init(secretKeySpec);
//        byte[] hmacBytes = mac.doFinal(data.getBytes());
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : hmacBytes) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) {
//                hexString.append('0');
//            }
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }

//    private final VnpayConfig vnPayConfig;
//
//    public VnpayServiceImpl(VnpayConfig vnPayConfig) {
//        this.vnPayConfig = vnPayConfig;
//    }
//
//    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) throws Exception {
//        try {
//            long amount = Long.parseLong(request.getParameter("amount")) * 100L; // VND nhân 100
//            String bankCode = request.getParameter("bankCode");
//
//            // Lấy cấu hình ban đầu
//            Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
//            vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
//            if (bankCode != null && !bankCode.isEmpty()) {
//                vnpParamsMap.put("vnp_BankCode", bankCode);
//            }
//            vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
//
//            System.out.println("VNPay Parameters: " + vnpParamsMap);
//
//            // Tạo hash data (dữ liệu sắp xếp và chưa encode)
//            String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false); // Không encode
//            System.out.println("Hash Data (raw): " + hashData);
//
//            // Tạo SecureHash
//            String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
//            System.out.println("Generated SecureHash: " + vnpSecureHash);
//
//            // Tạo query string đầy đủ (bao gồm SecureHash)
//            String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true) + "&vnp_SecureHash=" + vnpSecureHash;
//            String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
//
//            System.out.println("Query URL: " + queryUrl);
//
//            return PaymentDTO.VNPayResponse.builder()
//                    .code("ok")
//                    .message("success")
//                    .paymentUrl(paymentUrl)
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return PaymentDTO.VNPayResponse.builder()
//                    .code("error")
//                    .message("Failed to create payment")
//                    .build();
//        }
//    }
//        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
//        String bankCode = request.getParameter("bankCode");
//        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
//        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnpParamsMap.put("vnp_BankCode", bankCode);
//        }
//        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
//        //build query url
//        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
//        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
//        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
//        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
//        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
//        return PaymentDTO.VNPayResponse.builder()
//                .code("ok")
//                .message("success")
//                .paymentUrl(paymentUrl).build();
//    }

    private static final String secretKey = "NAWBHJNBBLZXSDESGVSNOKXXMQSFULRU"; // Thay bằng secretKey của bạn

    public void handleVnpayResponse(Map<String, String> params) {
        // Lấy SecureHash từ VNPAY
        String secureHash = params.get("vnp_SecureHash");
        // Loại bỏ SecureHash khỏi các tham số để tính toán lại
        params.remove("vnp_SecureHash");

        // Xác thực SecureHash
        if (isSecureHashValid(params, secureHash)) {
            String responseCode = params.get("vnp_ResponseCode");
            String orderCode = params.get("vnp_TxnRef");

            // Kiểm tra kết quả thanh toán
            if ("00".equals(responseCode)) {
                // Giao dịch thành công
                updateOrderPaymentStatus(orderCode, true);
            } else {
                // Giao dịch thất bại
                updateOrderPaymentStatus(orderCode, false);
            }
        } else {
            // Xử lý lỗi khi SecureHash không hợp lệ
            System.out.println("SecureHash không hợp lệ");
        }
    }

    private boolean isSecureHashValid(Map<String, String> params, String secureHash) {
        // Tính toán lại SecureHash từ các tham số
        String calculatedHash = calculateSecureHash(params);
        return calculatedHash.equals(secureHash);
    }

    private String calculateSecureHash(Map<String, String> params) {
        // Tạo chuỗi tham số để tính toán SecureHash
        return hashAllFields(params);
    }

    private String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                sb.append(fieldName).append("=").append(fieldValue);
                sb.append("&");
            }
        }
        sb.setLength(sb.length() - 1); // Loại bỏ ký tự "&" cuối cùng
        return hmacSHA512(secretKey, sb.toString());
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
    private void updateOrderPaymentStatus(String orderCode, boolean isPaid) {
        // Lấy trạng thái tương ứng từ bảng order_status
        String statusName = isPaid ? "PAID" : "UNPAID";

        // Tìm trạng thái từ bảng
        PaymentStatusM orderStatus = paymentStatusMRepository.findByStatusName(statusName);

        if (orderStatus != null) {
            // Cập nhật trạng thái cho đơn hàng (giả sử bạn có model Order với mối quan hệ tới OrderStatus)
            // order.setStatus(orderStatus);
            // orderRepository.save(order); // Lưu đơn hàng với trạng thái mới

            System.out.println("Cập nhật trạng thái thanh toán cho đơn hàng " + orderCode + " thành công: " + statusName);
        } else {
            System.out.println("Không tìm thấy trạng thái: " + statusName);
        }
    }
//    private void updateOrderPaymentStatus(String orderCode, boolean isPaid) {
//        // Cập nhật trạng thái thanh toán cho đơn hàng trong cơ sở dữ liệu
//        System.out.println("Cập nhật trạng thái thanh toán cho đơn hàng " + orderCode + " thành công: " + isPaid);
//    }
}
