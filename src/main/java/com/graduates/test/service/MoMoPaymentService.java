package com.graduates.test.service;



public interface MoMoPaymentService {
    String initiatePayment(Integer idorder, String amount, String orderInfo, String email) throws Exception;
}
