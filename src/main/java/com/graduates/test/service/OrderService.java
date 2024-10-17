package com.graduates.test.service;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.Order;
import com.graduates.test.model.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
   Order createOrder(Integer userId, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId,String phone,String receivingName,String note) throws Exception;

List<OrderResponse> getOrdersByUserIdAndOptionalStatus(Integer userId, Integer statusId);
   Page<OrderResponse> getAllOrdersForAdmin(Pageable pageable);
   void cancelOrder(Integer userId, Integer orderId) throws Exception;

   OrderResponse getOrderDetails(Integer orderId);

   boolean updateOrderStatus(Integer orderId, Integer statusId);
   // Page<OrderResponse> getOrdersByUserIdAndOptionalStatus(Integer userId, Integer statusId, Pageable pageable);
}
