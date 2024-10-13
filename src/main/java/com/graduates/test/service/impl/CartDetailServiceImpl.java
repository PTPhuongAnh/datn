package com.graduates.test.service.impl;

import com.graduates.test.model.CartDetail;
import com.graduates.test.resposity.CartDetailRepository;
import com.graduates.test.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CartDetailServiceImpl implements CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public boolean removeProductFromCart(Integer cartDetailId) {
        return false;
    }





}
