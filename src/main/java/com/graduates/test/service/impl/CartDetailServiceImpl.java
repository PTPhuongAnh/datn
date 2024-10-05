package com.graduates.test.service.impl;

import com.graduates.test.model.CartDetail;
import com.graduates.test.resposity.CartDetailRepository;
import com.graduates.test.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CartDetailServiceImpl implements CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;
    public boolean removeProductFromCart(Integer cartDetailId) {
        Optional<CartDetail> cartDetail = cartDetailRepository.findById(cartDetailId);
        if (cartDetail.isPresent()) {
            cartDetailRepository.delete(cartDetail.get());
            return true;
        }
        return false;
    }


}
