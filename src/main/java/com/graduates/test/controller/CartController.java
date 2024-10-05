package com.graduates.test.controller;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.model.Book;
import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.CartService;
import com.graduates.test.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;


    @PostMapping("/add/{bookId}/{quantity}")
    public ResponseEntity<?> addToCart(
            @PathVariable Integer bookId,
            @PathVariable int quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer userId = userDetails.getUserEntity().getIdUser(); // Lấy ID người dùng từ thông tin xác thực

        try {
            cartService.addToCart(userId, bookId, quantity);
            return ResponseHandler.responeBuilder("Book added to cart successfully.", HttpStatus.OK, true, null);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return ResponseHandler.responeBuilder("Book added to cart fail", HttpStatus.NOT_FOUND, false, null);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartResponse>> getCartByUserId(@PathVariable Integer userId) {
        List<CartResponse> cartResponses = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartResponses);
    }
}



