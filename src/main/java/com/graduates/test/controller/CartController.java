package com.graduates.test.controller;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.BookService;
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
    private BookService bookService;


//    @PostMapping("/add/{bookId}/{quantity}")
//    public ResponseEntity<?> addToCart(
//            @PathVariable Integer bookId,
//            @PathVariable int quantity,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        Integer userId = userDetails.getUserEntity().getIdUser(); // Lấy ID người dùng từ thông tin xác thực
//
//
//
//        try {
//            cartService.addToCart(userId, bookId, quantity);
//            return ResponseHandler.responeBuilder(HttpStatus.OK, true, null);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage();
//
//            if (errorMessage.contains("Số lượng muốn thêm vượt quá số lượng còn lại trong sách!")) {
//                // Nếu lỗi là do số lượng muốn thêm vượt quá số lượng trong kho
//                return ResponseHandler.responeBuilder(HttpStatus.OK, false, errorMessage);
//            } else if (errorMessage.contains("Sản phẩm hiện không còn hàng.")) {
//                // Nếu lỗi là do sản phẩm không còn hàng
//                return ResponseHandler.responeBuilder(HttpStatus.OK, false, errorMessage);
//            } else {
//                // Các lỗi khác
//                System.out.println("Unexpected error: " + errorMessage);
//                return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, "Đã xảy ra lỗi không mong muốn!");
//            }
//        }
@PostMapping("/add/{bookId}/{quantity}")
public ResponseEntity<?> addToCart(
        @PathVariable Integer bookId,
        @PathVariable int quantity,
        @RequestParam Integer userId) {

    try {
        // Sử dụng userId được gửi từ FE
        cartService.addToCart(userId, bookId, quantity);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, null);
    } catch (Exception e) {
        String errorMessage = e.getMessage();

        if (errorMessage.contains("Số lượng muốn thêm vượt quá số lượng còn lại trong sách!")) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false, errorMessage);
        } else if (errorMessage.contains("Sản phẩm hiện không còn hàng.")) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false, errorMessage);
        } else {
            System.out.println("Unexpected error: " + errorMessage);
            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, "Đã xảy ra lỗi không mong muốn!");
        }
    }
}







    @GetMapping("/listcart")
    public ResponseEntity<?> getCartByUser(@RequestParam Integer userId) {
        // Lấy giỏ hàng của người dùng dựa trên userId từ FE
        List<CartResponse> cartResponses = cartService.getCartByUserId(userId);

        // Trả về phản hồi
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, cartResponses);
    }

}



