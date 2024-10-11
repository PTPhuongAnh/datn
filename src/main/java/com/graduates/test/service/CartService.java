package com.graduates.test.service;

import com.graduates.test.dto.CartResponse;
import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;

import java.util.List;
import java.util.Optional;

public interface CartService {
    void addToCart(Integer userId, Integer bookId, int quantity) throws Exception;



    List<Cart> findByUser_idUser(Integer userId);


    List<CartResponse> getCartByUserId(Integer userId);

}

