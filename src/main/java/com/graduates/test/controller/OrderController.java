package com.graduates.test.controller;

import com.graduates.test.dto.BookRespone;
import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.Book;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @GetMapping("/list/order_user")
    public ResponseEntity<?> getOrdersByUserIdAndOptionalStatus(@RequestParam Integer userId) {

        List<OrderResponse> responses = orderService.getOrdersByUserId(userId);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, responses);
    }


    @DeleteMapping("/cancel")
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
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt"));
        Map<String, Object> response = orderService.getAllOrdersWithPagination(pageable);

        return ResponseHandler.responeBuilder(HttpStatus.OK,true,response);
                //new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy chi tiết đơn hàng cho user
    @GetMapping("/detail_user")
    public ResponseEntity<?> getOrderDetailForUser(@RequestParam Integer orderId, @RequestParam Integer userId) {
        OrderResponse orderResponse = orderService.getOrderDetailForUser(orderId, userId);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,orderResponse);
    }

    // Lấy chi tiết đơn hàng cho admin
    @GetMapping("/detail_admin")
    public ResponseEntity<?> getOrderDetailForAdmin(@RequestParam Integer orderId) {
        OrderResponse orderResponse = orderService.getOrderDetailForAdmin(orderId);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,orderResponse);
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
