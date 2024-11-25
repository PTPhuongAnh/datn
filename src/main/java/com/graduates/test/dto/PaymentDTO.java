package com.graduates.test.dto;

import lombok.Builder;
import lombok.Data;

public class PaymentDTO {
    @Data
    @Builder
    public static class VNPayResponse {
        private String code;
        private String message;
        private String paymentUrl;
    }
}
