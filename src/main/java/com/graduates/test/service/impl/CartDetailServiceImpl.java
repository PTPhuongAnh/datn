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

//
//    public boolean removeProductFromCart(Integer cartDetailId, Integer userId) {
////        Optional<CartDetail> cartDetail = cartDetailRepository.findByIdAndUserId(cartDetailId, userId);
////        if (cartDetail.isPresent()) {
////            cartDetailRepository.delete(cartDetail.get());
////            return true;
////        }
////        return false;
////    }


//    public boolean removeProductFromCart(Integer cartDetailId,Integer userId) {
//        Optional<CartDetail> cartDetail = cartDetailRepository.findByIdAndUserId(cartDetailId, userId);
//        if (cartDetail.isPresent()) {
//            cartDetailRepository.delete(cartDetail.get());
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean removeProductFromCart(Integer cartDetailId) {
//        return false;
//    }
}
