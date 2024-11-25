package com.graduates.test.service;

import com.graduates.test.dto.PaymentDTO;
import com.graduates.test.model.VnpayRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface VnpayService {
    PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) throws Exception;
}
