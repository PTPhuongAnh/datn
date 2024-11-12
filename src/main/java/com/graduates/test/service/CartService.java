package com.graduates.test.service;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CartService {
    void addToCartWithToken(String token, Integer bookId, int quantity) throws Exception;
    void addToCart(Integer userId, Integer bookId, int quantity) throws Exception;

    Page<CartResponse> getCartByUserToken(String token, Pageable pageable) throws Exception;

    List<Cart> findByUser_idUser(Integer userId);



    void updateCartQuantity(String token, Integer bookId, String operation) throws Exception;

    void deleteBooksFromCart(String token, List<Integer> idBooks) throws Exception;

 //   Page<CartResponse> getCartByUserId(Integer userId, Pageable pageable) throws Exception;
}

