package com.graduates.test.service.impl;

import com.graduates.test.dto.MomoResponse;
import com.graduates.test.model.MomoRequest;
import com.graduates.test.model.SignatureUtil;
import com.graduates.test.service.MomoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MomoServiceImpl implements MomoService {
    @Value("${momo.accessKey}")
    private String accessKey;
    @Value("${momo.secretKey}")
    private String secretKey;
    @Value("${momo.partnerCode}")
    private String partnerCode;
    @Value("${momo.endpoint}")
    private String momoEndpoint;
    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    @Value("${momo.redirectUrl}")
    private String redirectUrl;

    public MomoResponse createPayment(String amount, String orderId) throws Exception {
        String requestId = partnerCode + System.currentTimeMillis();
        String extraData = "";
        String rawSignature = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + "pay with MoMo" +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + "payWithMethod";
        String signature = SignatureUtil.hmacSHA256(rawSignature, secretKey);
        MomoRequest momoRequest = new MomoRequest();
        momoRequest.setPartnerCode(partnerCode);
        momoRequest.setPartnerName("Test");
        momoRequest.setStoreId("MomoTestStore");
        momoRequest.setRequestId(requestId);
        momoRequest.setAmount(amount);
        momoRequest.setOrderId(orderId);
        momoRequest.setOrderInfo("pay with MoMo");
        momoRequest.setRedirectUrl(redirectUrl);
        momoRequest.setIpnUrl(ipnUrl);
        momoRequest.setLang("vi");
        momoRequest.setRequestType("payWithMethod");
        momoRequest.setAutoCapture(true);
        momoRequest.setExtraData(extraData);
        momoRequest.setOrderGroupId("");
        momoRequest.setSignature(signature);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(momoEndpoint, momoRequest, MomoResponse.class);
    }
}
