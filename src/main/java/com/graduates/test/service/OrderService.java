package com.graduates.test.service;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.Order;
import com.graduates.test.model.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface OrderService {
   Order createOrder( String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId,String phone,String receivingName,String note) throws Exception;


   void cancelOrder(Integer userId, Integer orderId) throws Exception;



   boolean updateOrderStatus(Integer orderId, Integer statusId);



   Map<String, Object> getMonthlyRevenue();

   Map<String, Object> getStatistics();

    List<OrderResponse> getOrdersByUserId(Integer userId);

    Order getOrderById(Integer idOrder);

    Map<String, Object> getAllOrdersWithPagination(Pageable pageable);



    OrderResponse getOrderDetailForAdmin(Integer orderId);

    OrderResponse getOrderDetailForUser(Integer orderId, Integer userId);

}
