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
   Order createOrder(Integer userId, String shippingAddress, List<Integer> selectedCartDetailIds, Integer paymentId, Integer shipmentId,String phone,String receivingName,String note) throws Exception;

//List<OrderResponse> getOrdersByUserIdAndOptionalStatus(Integer userId, Integer statusId);
 //  Page<OrderResponse> getAllOrdersForAdmin(Pageable pageable);
   void cancelOrder(Integer userId, Integer orderId) throws Exception;

  // OrderResponse getOrderDetails(Integer orderId);

   boolean updateOrderStatus(Integer orderId, Integer statusId);

  //  Map<String, Object> getSalesStatistics(String period);

   Map<String, Object> getMonthlyRevenue();

   Map<String, Object> getStatistics();

    List<OrderResponse> getOrdersByUserId(Integer userId);

    Order getOrderById(Integer idOrder);

    Map<String, Object> getAllOrdersWithPagination(Pageable pageable);

    OrderResponse getOrderDetailForUser(Integer orderId, Integer userId);

    OrderResponse getOrderDetailForAdmin(Integer orderId);

    //  Page<OrderResponse> getAllOrders(Pageable pageable);

 //   Page<Order> findAllOrders(Pageable pageable);
    // Page<OrderResponse> getOrdersByUserIdAndOptionalStatus(Integer userId, Integer statusId, Pageable pageable);
}
