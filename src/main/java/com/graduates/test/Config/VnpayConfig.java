package com.graduates.test.Config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Configuration
public class VnpayConfig {


    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:3000/checkout-success";
    public static String vnp_TmnCode = "V9TZGVS9";
    public static String secretKey = "NAWBHJNBBLZXSDESGVSNOKXXMQSFULRU";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String md5(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    public static String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    //Util for VNPAY
    public static String hashAllFields(Map fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return hmacSHA512(secretKey,sb.toString());
    }

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

//@Value("${payment.vnPay.url}")
//private String vnp_PayUrl;
//
//    @Value("${payment.vnPay.returnUrl}")
//    private String vnp_ReturnUrl;
//
//    @Value("${payment.vnPay.tmnCode}")
//    private String vnp_TmnCode;
//
//    @Value("${payment.vnPay.secretKey}")
//    private String secretKey;
//
//    @Value("${payment.vnPay.version}")
//    private String vnp_Version;
//
//    @Value("${payment.vnPay.command}")
//    private String vnp_Command;
//
//    @Value("${payment.vnPay.orderType}")
//    private String orderType;
//
//    public Map<String, String> getVNPayConfig() {
//        Map<String, String> vnpParamsMap = new HashMap<>();
//        vnpParamsMap.put("vnp_Version", this.vnp_Version);
//        vnpParamsMap.put("vnp_Command", this.vnp_Command);
//        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
//        vnpParamsMap.put("vnp_CurrCode", "VND");
//        vnpParamsMap.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8)); // Giới hạn 8 ký tự
//        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang #" + VNPayUtil.getRandomNumber(6));
//        vnpParamsMap.put("vnp_OrderType", this.orderType);
//        vnpParamsMap.put("vnp_Locale", "vn");
//        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
//
//        // Thời gian tạo và hết hạn
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        vnpParamsMap.put("vnp_CreateDate", formatter.format(calendar.getTime()));
//        calendar.add(Calendar.MINUTE, 15);
//        vnpParamsMap.put("vnp_ExpireDate", formatter.format(calendar.getTime()));
//
//        return vnpParamsMap;
////    }
}
