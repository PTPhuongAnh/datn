package com.graduates.test.service;

public interface PaymentStatusMService {
    void updateOrderPaymentStatus(String orderCode, boolean isPaid);
}
