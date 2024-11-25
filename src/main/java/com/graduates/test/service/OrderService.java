package com.graduates.test.service;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.Order;
import com.graduates.test.model.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
   Order createOrder(String token, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId,String phone,String receivingName,String note,Integer voucherId) throws Exception;


   void cancelOrder(String token, Integer orderId) throws Exception;



   boolean updateOrderStatus(Integer orderId, Integer statusId);



   Map<String, Object> getMonthlyRevenue();

   Map<String, Object> getStatistics();

    List<OrderResponse> getOrdersByUserId(String token);

    Order getOrderById(Integer idOrder);

  //  Map<String, Object> getAllOrdersWithPagination(Pageable pageable);



    OrderResponse getOrderDetailForAdmin(Integer orderId);

    OrderResponse getOrderDetailForUser(Integer orderId, String token);

    Map<String, Object> getAllOrdersWithPagination(Pageable pageable, String orderCode, LocalDateTime startDate, LocalDateTime endDate);
   // Integer getOrderIdByOrderCode(String orderCode);

    //  String getOrderIdByOrderCode(String orderCode);
}
