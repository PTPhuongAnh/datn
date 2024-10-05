package com.graduates.test.service.impl;

import com.graduates.test.model.UserEntity;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserResposity userRepository;

//    @Override
//    public Page<UserEntity> searchUser(String username, String email, String fullname, String dob, String phone, int page, int sizes) {
//        Pageable pageable = PageRequest.of(page, sizes);
//        return userRepository.searchUser(username, email, fullname, dob, phone,  pageable);
//    }


    @Override
    public Page<UserEntity> searchUser(String username, String email, String fullname, String dob, String phone, String street, String city, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
        return  userRepository.searchUser(username, email, fullname, dob, phone,street,phone,  pageable);
    }
}
