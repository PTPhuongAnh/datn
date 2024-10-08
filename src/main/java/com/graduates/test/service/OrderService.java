package com.graduates.test.service;

import com.graduates.test.model.Order;
import com.graduates.test.model.OrderRequest;

import java.util.List;

public interface OrderService {
   Order createOrder(Integer userId, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId) throws Exception;
    //  Order createOrder(OrderRequest orderRequest) throws Exception;
    //Order createOrder(Integer userId, Integer paymentId, Integer shipmentId, String shippingAddress, List<Integer> selectedCartDetailIds) throws Exception;
  //  Order createOrderFromCart(Integer userId, List<Integer> selectedCartDetailIds, String paymentMethod, String shippingMethod, String shippingAddress) throws Exception;
}
