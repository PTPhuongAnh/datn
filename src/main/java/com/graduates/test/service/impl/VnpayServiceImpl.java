package com.graduates.test.service.impl;

import com.graduates.test.Config.VNPayUtil;
import com.graduates.test.Config.VnpayConfig;
import com.graduates.test.dto.PaymentDTO;
import com.graduates.test.model.VnpayRequest;
import com.graduates.test.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class VnpayServiceImpl implements VnpayService {
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

    private final VnpayConfig vnPayConfig;

    public VnpayServiceImpl(VnpayConfig vnPayConfig) {
        this.vnPayConfig = vnPayConfig;
    }

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) throws Exception {
        try {
            long amount = Long.parseLong(request.getParameter("amount")) * 100L; // VND nhân 100
            String bankCode = request.getParameter("bankCode");

            // Lấy cấu hình ban đầu
            Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
            vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
            if (bankCode != null && !bankCode.isEmpty()) {
                vnpParamsMap.put("vnp_BankCode", bankCode);
            }
            vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

            System.out.println("VNPay Parameters: " + vnpParamsMap);

            // Tạo hash data (dữ liệu sắp xếp và chưa encode)
            String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false); // Không encode
            System.out.println("Hash Data (raw): " + hashData);

            // Tạo SecureHash
            String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            System.out.println("Generated SecureHash: " + vnpSecureHash);

            // Tạo query string đầy đủ (bao gồm SecureHash)
            String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true) + "&vnp_SecureHash=" + vnpSecureHash;
            String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

            System.out.println("Query URL: " + queryUrl);

            return PaymentDTO.VNPayResponse.builder()
                    .code("ok")
                    .message("success")
                    .paymentUrl(paymentUrl)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return PaymentDTO.VNPayResponse.builder()
                    .code("error")
                    .message("Failed to create payment")
                    .build();
        }
    }
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
}