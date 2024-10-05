package com.graduates.test.controller;

import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class CartDetailController {
@Autowired
    private CartDetailService cartDetailService;

    @DeleteMapping("/detail/{cartDetailId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable Integer cartDetailId) {
        boolean isRemoved = cartDetailService.removeProductFromCart(cartDetailId);
        if (isRemoved) {
            return ResponseHandler.responeBuilder("Product removed from cart successfully.",HttpStatus.OK,true,null);
        } else {

            return ResponseHandler.responeBuilder("Product not found in cart",HttpStatus.OK,true,null);
        }
    }

}
