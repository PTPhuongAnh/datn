package com.graduates.test.controller;

import com.graduates.test.Config.VnpayConfig;
import com.graduates.test.dto.PaymentDTO;
import com.graduates.test.model.Order;
import com.graduates.test.model.VnpayRequest;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.OrderService;
import com.graduates.test.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
//@RequestMapping("/api")
public class VnpayController {
    @Autowired
    private OrderService orderService;
//    @GetMapping("/pay")
//    public String getPay() throws UnsupportedEncodingException {
//
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String orderType = "other";
//        long amount = 20000*100;
//        String bankCode = "NCB";
//
//        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
//        String vnp_IpAddr = "127.0.0.1";
//
//        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
//
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//
//        vnp_Params.put("vnp_BankCode", bankCode);
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
//        vnp_Params.put("vnp_OrderType", orderType);
//
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        List fieldNames = new ArrayList(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = (String) vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
//
//        return paymentUrl;
//    }


    @PostMapping("/pay")
    public ResponseEntity<?>  getPay(Integer idorder, long amount1) throws UnsupportedEncodingException {
        Order order = orderService.getOrderById(idorder);
        String orderId = order.getOrderCode();
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
       // long amount = 20000*100;vnp_ResponseCode=00
        long amount= amount1*1000;
        String bankCode = "NCB";

//        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_TxnRef = orderId;
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;

       // return paymentUrl;
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,paymentUrl);
    }





//private final VnpayService paymentService;
////
//    @Autowired
//    public VnpayController(VnpayService paymentService) {
//        this.paymentService = paymentService;
//    }
//
//    /**
//     * API để tạo thanh toán qua VNPAY
//     */
//    @PostMapping("/vn-pay")
//    public ResponseEntity<PaymentDTO.VNPayResponse> createVnPayPayment(HttpServletRequest request) throws Exception {
//        PaymentDTO.VNPayResponse response = paymentService.createVnPayPayment(request);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * API callback để xử lý kết quả trả về từ VNPAY
//     */
//    @GetMapping("/vn-pay-callback")
//    public ResponseEntity<String> vnPayCallback(HttpServletRequest request) {
//        String vnpTxnRef = request.getParameter("vnp_TxnRef");
//        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
//
//        if ("00".equals(vnpResponseCode)) {
//            // Thanh toán thành công
//            return ResponseEntity.ok("Payment success! Transaction reference: " + vnpTxnRef);
//        } else {
//            // Thanh toán thất bại hoặc bị hủy
//            return ResponseEntity.status(400).body("Payment failed! Response code: " + vnpResponseCode);
//        }
//    }
}
