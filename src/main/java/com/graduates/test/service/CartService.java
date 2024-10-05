package com.graduates.test.service;

import com.graduates.test.model.Cart;

import java.util.Optional;

public interface CartService {
    void addToCart(Integer userId, Integer bookId, int quantity) throws Exception;


    Optional<Cart> getCartByUserId(Integer userId);
}

