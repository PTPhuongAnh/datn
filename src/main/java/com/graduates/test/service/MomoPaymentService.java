package com.graduates.test.service;

import com.graduates.test.model.Order;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MomoPaymentService {
  //  void createMomoPayment();
//    String createMomoPayment(Order order);
  String generateSignature(String rawData, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException;
}
