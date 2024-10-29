package com.graduates.test.service;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CartService {
    void addToCart(Integer userId, Integer bookId, int quantity) throws Exception;



    List<Cart> findByUser_idUser(Integer userId);



    void updateCartQuantity(Integer userId, Integer bookId, String operation) throws Exception;

    void deleteBooksFromCart(Integer userId, List<Integer> idBooks);

    Page<CartResponse> getCartByUserId(Integer userId, Pageable pageable) throws Exception;
}

