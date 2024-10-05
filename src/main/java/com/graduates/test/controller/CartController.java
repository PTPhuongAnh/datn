package com.graduates.test.controller;

import com.graduates.test.dto.AddToCartRequest;
import com.graduates.test.model.Cart;
import com.graduates.test.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add/{userId}/{bookId}/{quantity}")
    public ResponseEntity<String> addToCart(
            @PathVariable Integer userId,
            @PathVariable Integer bookId,
            @PathVariable int quantity) {
        try {
            cartService.addToCart(userId, bookId, quantity);
            return ResponseEntity.ok("Book added to cart successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Integer userId) {
        Optional<Cart> cartOptional = cartService.getCartByUserId(userId);
        if (cartOptional.isPresent()) {
            return ResponseEntity.ok(cartOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    }



