package com.graduates.test.controller;

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
            @RequestBody OrderRequest orderRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer userId = userDetails.getUserEntity().getIdUser(); // Lấy ID người dùng từ thông tin xác thực

        try {
            // Gọi service để tạo đơn hàng từ giỏ hàng
            Order newOrder = orderService.createOrder(
                    userId,
                    orderRequest.getShippingAddress(),
                    orderRequest.getSelectedCartDetailIds(),
                    orderRequest.getPaymentId(),
                    orderRequest.getShipmentId()
            );

            return ResponseHandler.responeBuilder(HttpStatus.OK, true, "ORDER success");
        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false, "Error creating order: " + e.getMessage());
        }
    }
}
