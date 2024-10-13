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


  //  List<CartResponse> getCartByUserId(Integer userId) throws Exception;
   // Page<CartResponse> getCartByUserId(Integer userId,int page,int size);

    void updateCartQuantity(Integer userId, Integer bookId, String operation) throws Exception;
    void deleteBookFromCart(Integer idBook);

    Page<CartResponse> getCartByUserId(Integer userId, Pageable pageable) throws Exception;
}

