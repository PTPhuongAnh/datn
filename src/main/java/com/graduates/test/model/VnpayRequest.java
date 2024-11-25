package com.graduates.test.model;

import lombok.Data;

@Data
public class VnpayRequest {
    private String orderId;
    private String amount;
    private String orderInfo;
    private String bankCode;


}
