package com.graduates.test.service;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.Order;
import com.graduates.test.model.OrderRequest;

import java.util.List;

public interface OrderService {
   Order createOrder(Integer userId, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId,String phone,String receivingName) throws Exception;
   List<OrderResponse> getOrderByUserId(Integer userId);
}
