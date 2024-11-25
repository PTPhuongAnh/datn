package com.graduates.test.Config;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class VNPayUtil {
    public static String getRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Chỉ số
        }
        return sb.toString();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getPaymentURL(Map<String, String> params, boolean forQuery) {
        return params.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty()) // Loại bỏ giá trị null hoặc rỗng
                .sorted(Map.Entry.comparingByKey()) // Sắp xếp theo key A-Z
                .map(entry -> {
                    try {
                        String key = URLEncoder.encode(entry.getKey(), "UTF-8");
                        String value = URLEncoder.encode(entry.getValue(), "UTF-8");
                        return key + "=" + value;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(forQuery ? "&" : ""));
    }

    public static String hmacSHA512(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA512");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes("UTF-8"));
        StringBuilder hashString = new StringBuilder();
        for (byte b : hash) {
            hashString.append(String.format("%02x", b));
        }
        return hashString.toString();
    }



}
