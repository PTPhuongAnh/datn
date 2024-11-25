package com.graduates.test.controller;

import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.BookRespone;
import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.*;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.OrderService;
import com.graduates.test.service.UserService;
import com.graduates.test.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Autowired
    private JwtService jwtService;
@Autowired
    private UserResposity userResposity;

    public OrderController(OrderService orderService, UserService userService, JwtService jwtService, UserResposity userResposity) {
        this.orderService = orderService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userResposity = userResposity;
    }

    @PostMapping("/create")

    public ResponseEntity<?> createOrder(
            @RequestHeader("Authorization") String token,
            @RequestParam("shippingAddress") String shippingAddress, // Địa chỉ giao hàng
            @RequestParam("selectedCartDetailIds") List<Integer> selectedCartDetailIds, // Danh sách ID chi tiết giỏ hàng đã chọn
            @RequestParam("paymentId") Integer paymentId, // ID thanh toán
            @RequestParam("shipmentId") Integer shipmentId ,// ID vận chuyển
            @RequestParam("phone") String phone,
            @RequestParam("receivingName") String receivingName,
            @RequestParam("note") String note,
            @RequestParam(value = "voucher",required = false) Integer voucherId

            ) {

       // Integer userId = userDetails.getUserEntity().getIdUser(); // Lấy ID người dùng từ thông tin xác thực

        try {
         //   String token = authorizationHeader.substring(7); // "Bearer " có độ dài 7 ký tự
            token = token.replace("Bearer ", "");
            // Gọi service để tạo đơn hàng từ giỏ hàng
            Order newOrder = orderService.createOrder(
                    token,
                    shippingAddress,
                    selectedCartDetailIds,
                    paymentId,
                    shipmentId,
                    phone,
                    receivingName,
                    note,
                    voucherId


            );

            return ResponseHandler.responeBuilder(HttpStatus.OK, true, "ORDER success");
        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false,  e.getMessage());
        }
    }


    @GetMapping("/list/order_user")
    public ResponseEntity<?> getOrdersByUserIdAndOptionalStatus( @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        List<OrderResponse> responses = orderService.getOrdersByUserId(token);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, responses);
    }


    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer orderId) {
        try {
            token = token.replace("Bearer ", "");
            // Gọi service để hủy đơn hàng
            orderService.cancelOrder(token, orderId);
            return ResponseHandler.responeBuilder(HttpStatus.OK,true,"Order canceled successfully.");
        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.OK,false,
                    e.getMessage());
        }
    }
    @GetMapping("/list/admin")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "orderCode", required = false) String orderCode,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime endDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Tìm kiếm các đơn hàng trong khoảng ngày
       // List<Order> orders = orderService.getAllOrdersWithPagination(orderCode, startDate, endDate);
        Map<String, Object> response = orderService.getAllOrdersWithPagination(pageable, orderCode, startDate, endDate);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, response);
    }
    // Lấy chi tiết đơn hàng cho user
    @GetMapping("/detail_user")
    public ResponseEntity<?> getOrderDetailForUser(@RequestParam Integer orderId,@RequestHeader("Authorization") String token ) {
        token = token.replace("Bearer ", "");
        OrderResponse orderResponse = orderService.getOrderDetailForUser(orderId,token);
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
                                               @RequestHeader("Authorization") String token) {
        try {
            // Lấy token từ header "Authorization" (cắt bỏ phần "Bearer ")
//            String token = authorizationHeader.substring(7); // "Bearer " có độ dài 7 ký tự
            token = token.replace("Bearer ", "");
            // Lấy username từ token và xác thực quyền admin
//            String username = jwtService.extractUsername(token); // Lấy username từ token


            // Cập nhật trạng thái đơn hàng
            boolean isUpdated = orderService.updateOrderStatus(orderId, statusId);

            if (isUpdated) {
                return ResponseHandler.responeBuilder(HttpStatus.OK, true, "Order status updated successfully");
            } else {
                return ResponseHandler.responeBuilder(HttpStatus.BAD_REQUEST, false,
                        "Cannot update status because the order is completed or does not exist.");
            }
        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, e.getMessage());
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
