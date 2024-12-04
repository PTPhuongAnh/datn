package com.graduates.test.service;


import jakarta.persistence.criteria.CriteriaBuilder;

public interface MoMoPaymentService {
    String initiatePayment(Integer idorder, String amount, String orderInfo, String email) throws Exception;
  // String initiateATMRequest(Integer idorder, String amount, String email) throws Exception;
}
