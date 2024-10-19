package com.graduates.test.controller;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.Order;
import com.graduates.test.model.OrderRequest;
import com.graduates.test.model.UserEntity;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.OrderService;
import com.graduates.test.service.UserService;
import com.graduates.test.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService; // Inject OrderService
    @Autowired
    private UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/create")

    public ResponseEntity<?> createOrder(
            @RequestParam("userId") Integer userId, // Lấy userId từ tham số
            @RequestParam("shippingAddress") String shippingAddress, // Địa chỉ giao hàng
            @RequestParam("selectedCartDetailIds") List<Integer> selectedCartDetailIds, // Danh sách ID chi tiết giỏ hàng đã chọn
            @RequestParam("paymentId") Integer paymentId, // ID thanh toán
            @RequestParam("shipmentId") Integer shipmentId ,// ID vận chuyển
            @RequestParam("phone") String phone,
            @RequestParam("receivingName") String receivingName,
            @RequestParam("note") String note

           ) {

       // Integer userId = userDetails.getUserEntity().getIdUser(); // Lấy ID người dùng từ thông tin xác thực

        try {
            // Gọi service để tạo đơn hàng từ giỏ hàng
            Order newOrder = orderService.createOrder(
                    userId,
                    shippingAddress,
                    selectedCartDetailIds,
                    paymentId,
                    shipmentId,
                    phone,
                    receivingName,
                    note


            );

            return ResponseHandler.responeBuilder(HttpStatus.OK, true, "ORDER success");
        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false,  e.getMessage());
        }
    }


    @GetMapping("/list")
    public ResponseEntity<?> getOrdersByUserIdAndOptionalStatus(@RequestParam Integer userId,
                                                                @RequestParam Integer statusId) {

        List<OrderResponse> responses = orderService.getOrdersByUserIdAndOptionalStatus(userId,statusId);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, responses);
    }


    @PutMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
            @RequestParam Integer userId,
            @RequestParam Integer orderId) {
        try {
            // Gọi service để hủy đơn hàng
            orderService.cancelOrder(userId, orderId);
            return ResponseHandler.responeBuilder(HttpStatus.OK,true,"Order canceled successfully.");
        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.OK,false,
                    e.getMessage());
        }
    }


    @GetMapping("/list/admin")
    public ResponseEntity<?> getAllOrdersForAdmin(@RequestParam Integer userId,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        // Kiểm tra quyền admin
        if (!userService.isAdmin(userId)) {
            return ResponseHandler.responeBuilder(HttpStatus.OK,true,
                    "You are not authorized to view orders");
        }

        // Nếu là admin, lấy danh sách đơn hàng với phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponse> orders = orderService.getAllOrdersForAdmin(pageable);

        // Chuyển đổi kết quả thành phản hồi với thông tin phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders.getContent());
        response.put("currentPage", orders.getNumber());
        response.put("totalItems", orders.getTotalElements());
        response.put("totalPages", orders.getTotalPages());

        return ResponseHandler.responeBuilder(HttpStatus.OK,true,response);
    }


    @GetMapping("/details")
    public ResponseEntity<?> getOrderDetails(@RequestParam("orderId") Integer orderId,
                                             @RequestParam("userId") Integer userId) {
        // Kiểm tra quyền admin
        if (!userService.isAdmin(userId)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("You are not authorized to view this order");
            return  ResponseHandler.responeBuilder(HttpStatus.OK,true,"You are not authorized to view this order");
        }
        // Nếu là admin, lấy chi tiết đơn hàng
        OrderResponse orderResponse = orderService.getOrderDetails(orderId);
        if (orderResponse != null) {
          //  return ResponseEntity.ok(orderResponse);
            return  ResponseHandler.responeBuilder(HttpStatus.OK,true,orderResponse);
        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Order not found");
            return  ResponseHandler.responeBuilder(HttpStatus.OK,false,"Order not found");
        }
    }


    @PutMapping("/update-status")
    public ResponseEntity<?> updateOrderStatus(@RequestParam("orderId") Integer orderId,
                                               @RequestParam("statusId") Integer statusId,
                                               @RequestParam("userId") Integer userId) {
        // Kiểm tra quyền admin
        if (!userService.isAdmin(userId)) {
            return ResponseHandler.responeBuilder(HttpStatus.OK,false,
                    "You are not authorized to update this order");
        }

        // Cập nhật trạng thái đơn hàng dựa vào statusId
        boolean isUpdated = orderService.updateOrderStatus(orderId, statusId);

        if (isUpdated) {
           return ResponseHandler.responeBuilder(HttpStatus.OK,true,"Order status updated successfully");
        } else {
            return ResponseHandler.responeBuilder(HttpStatus.OK,false,
                    "Order not found or status not updated");
        }
    }



    @GetMapping("/sales")
    public ResponseEntity<?> getStatistics() {
        Map<String, Object> statistics = orderService.getStatistics();
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,statistics);
    }
        @GetMapping("/revenue-chart")
    public ResponseEntity<?> getRevenueChart() {
        Map<String, Object> chartData = orderService.getMonthlyRevenue();
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,chartData);
    }



}
