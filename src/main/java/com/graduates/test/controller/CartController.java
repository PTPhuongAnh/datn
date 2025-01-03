package com.graduates.test.controller;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.model.Book;
import com.graduates.test.model.CartDetail;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.BookService;
import com.graduates.test.service.CartService;
import com.graduates.test.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    private BookService bookService;



//@PostMapping("/add")
//public ResponseEntity<?> addToCart(
//        @RequestParam Integer bookId,
//        @RequestParam int quantity,
//        @RequestParam Integer userId) {
//
//    try {
//        // Sử dụng userId được gửi từ FE
//        cartService.addToCart(userId, bookId, quantity);
//        return ResponseHandler.responeBuilder(HttpStatus.OK, true, null);
//    } catch (Exception e) {
//        String errorMessage = e.getMessage();
//
//        if (errorMessage.contains("Số lượng muốn thêm vượt quá số lượng còn lại trong sách!")) {
//            return ResponseHandler.responeBuilder(HttpStatus.OK, false, errorMessage);
//        } else if (errorMessage.contains("Sản phẩm hiện không còn hàng.")) {
//            return ResponseHandler.responeBuilder(HttpStatus.OK, false, errorMessage);
//        } else {
//            System.out.println("Unexpected error: " + errorMessage);
//            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, "Đã xảy ra lỗi không mong muốn!");
//        }
//    }
//}


    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Integer bookId,
            @RequestParam int quantity,
            @RequestHeader("Authorization") String token) {

        try {
            token = token.replace("Bearer ", "");
            // Gọi phương thức addToCartWithToken trong service
            cartService.addToCartWithToken(token, bookId, quantity);
            return ResponseHandler.responeBuilder(HttpStatus.OK, true, null);

        } catch (Exception e) {
            String errorMessage = e.getMessage();

            if (errorMessage.contains("Số lượng muốn thêm vượt quá số lượng còn lại trong sách!")) {
                return ResponseHandler.responeBuilder1(HttpStatus.OK, false, errorMessage);
            } else if (errorMessage.contains("Sản phẩm hiện không còn hàng.")) {
                return ResponseHandler.responeBuilder1(HttpStatus.OK, false, errorMessage);
            } else {
                System.out.println("Unexpected error: " + errorMessage);
                return ResponseHandler.responeBuilder1(HttpStatus.OK, false, e.getMessage());
            }
        }
    }
//    @GetMapping("/list")
//
//    public ResponseEntity<?> getCartByUser(
//            @RequestParam Integer userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) throws Exception {
//
//        // Tạo đối tượng Pageable với trang và kích thước từ tham số yêu cầu
//        Pageable pageable = PageRequest.of(page, size);
//        Page<CartResponse> cartResponses = cartService.getCartByUserId(userId, pageable);
//        // Gọi service để lấy danh sách sách có phân trang
//        List<CartResponse> cartItems = cartResponses.getContent();
//        Map<String, Object> response = new HashMap<>();
//        response.put("cartItems", cartItems);
//        response.put("currentPage", cartResponses.getNumber());
//        response.put("totalItems", cartResponses.getTotalElements());
//        response.put("totalPages", cartResponses.getTotalPages());
//
//        return ResponseHandler.responeBuilder(HttpStatus.OK, true, response);
//    }

    @GetMapping("/list")
    public ResponseEntity<?> getCartByUser(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            // Loại bỏ tiền tố "Bearer " nếu có trong token
            token = token.replace("Bearer ", "");

            // Tạo đối tượng Pageable với trang và kích thước từ tham số yêu cầu
            Pageable pageable = PageRequest.of(page, size);

            // Gọi service để lấy danh sách sách có phân trang dựa trên token
            Page<CartResponse> cartResponses = cartService.getCartByUserToken(token, pageable);

            // Chuẩn bị response
            List<CartResponse> cartItems = cartResponses.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("cartItems", cartItems);
            response.put("currentPage", cartResponses.getNumber());
            response.put("totalItems", cartResponses.getTotalElements());
            response.put("totalPages", cartResponses.getTotalPages());

            return ResponseHandler.responeBuilder(HttpStatus.OK, true, response);

        } catch (Exception e) {
            // Trả về thông báo lỗi nếu có ngoại lệ
            return ResponseHandler.responeBuilder1(HttpStatus.OK, false, e.getMessage());
        }
    }


    @PutMapping("/update-quantity")
    public ResponseEntity<?> updateCartQuantity(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer bookId,
            @RequestParam String operation) {

        try {
            // Loại bỏ "Bearer " nếu có trong token
            token = token.replace("Bearer ", "");

            // Gọi service để cập nhật số lượng sản phẩm trong giỏ hàng
            cartService.updateCartQuantity(token, bookId, operation);
            return ResponseHandler.responeBuilder(HttpStatus.OK, true, "Số lượng sản phẩm đã được cập nhật.");
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Không đủ số lượng trong kho")) {
                return ResponseHandler.responeBuilder1(HttpStatus.OK, false, errorMessage);
            }
            //return ResponseHandler.responeBuilder1(HttpStatus.OK, false, "Không thể cập nhật số lượng sản phẩm!");
            return ResponseHandler.responeBuilder1(HttpStatus.OK, false, e.getMessage());
        }
    }



@DeleteMapping("/delete")
public ResponseEntity<?> deleteBooksFromCart(
        @RequestHeader("Authorization") String token, // Lấy token từ header
        @RequestParam List<Integer> idBook) {

    try {
        // Lấy token từ header và loại bỏ tiền tố "Bearer " nếu có
         token = token.replace("Bearer ", "");
        cartService.deleteBooksFromCart(token, idBook); // Gọi service để xóa sách
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, "Sách đã được xóa khỏi giỏ hàng.");

    } catch (Exception e) {
        return ResponseHandler.responeBuilder1(HttpStatus.OK, false, e.getMessage());
    }
}
}







