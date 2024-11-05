package com.graduates.test.service;

import com.graduates.test.dto.MomoResponse;

public interface MomoService {
    MomoResponse createPayment(String amount, String orderId) throws Exception;
}
