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
    private UserService userService;

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

        // Lấy giỏ hàng của người dùng dựa trên userId từ FE

        List<OrderResponse> responses = orderService.getOrdersByUserIdAndOptionalStatus(userId,statusId);

        // Trả về phản hồi
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, responses);
    }


    @PutMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
            @RequestParam Integer userId,
            @RequestParam Integer orderId) {
        try {
            // Gọi service để hủy đơn hàng
            orderService.cancelOrder(userId, orderId);
            return ResponseEntity.ok("Order canceled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @GetMapping("/list/admin")
    public ResponseEntity<?> getAllOrdersForAdmin(@RequestParam Integer userId,
                                                  @RequestParam int page,
                                                  @RequestParam int size) {
        // Kiểm tra quyền admin
        if (!userService.isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to view orders");
        }

        // Nếu là admin, lấy danh sách đơn hàng với phân trang
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getAllOrdersForAdmin(pageable);

        // Chuyển đổi kết quả thành phản hồi với thông tin phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders.getContent());
        response.put("currentPage", orders.getNumber());
        response.put("totalItems", orders.getTotalElements());
        response.put("totalPages", orders.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
