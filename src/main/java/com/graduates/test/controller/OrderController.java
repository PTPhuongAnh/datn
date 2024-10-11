package com.graduates.test.controller;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.dto.OrderResponse;
import com.graduates.test.model.Order;
import com.graduates.test.model.OrderRequest;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.OrderService;
import com.graduates.test.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService; // Inject OrderService

    @PostMapping("/create")

    public ResponseEntity<?> createOrder(
            @RequestParam("userId") Integer userId, // Lấy userId từ tham số
            @RequestParam("shippingAddress") String shippingAddress, // Địa chỉ giao hàng
            @RequestParam("selectedCartDetailIds") List<Integer> selectedCartDetailIds, // Danh sách ID chi tiết giỏ hàng đã chọn
            @RequestParam("paymentId") Integer paymentId, // ID thanh toán
            @RequestParam("shipmentId") Integer shipmentId ,// ID vận chuyển
            @RequestParam("phone") String phone,
            @RequestParam("receivingName") String receivingName


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
                    receivingName

            );

            return ResponseHandler.responeBuilder(HttpStatus.OK, true, "ORDER success");
        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false, "Error creating order: " + e.getMessage());
        }
    }


    @GetMapping("/list")
    public ResponseEntity<?> getOrderByUser(@RequestParam Integer userId) {
        // Lấy giỏ hàng của người dùng dựa trên userId từ FE
        List<OrderResponse> responses = orderService.getOrderByUserId(userId);

        // Trả về phản hồi
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, responses);
    }
}
