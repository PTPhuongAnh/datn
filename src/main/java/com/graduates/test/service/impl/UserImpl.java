package com.graduates.test.service.impl;

import com.graduates.test.model.UserEntity;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.graduates.test.model.Role;

import java.util.List;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserResposity userRepository;

    public List<String> getRolesByUserId(Integer userId) {
        // Tìm người dùng theo userId
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Trả về danh sách tên vai trò của người dùng
        return user.getRoles().stream()
                .map(Role::getName)
                .toList();
    }


    @Override
    public Page<UserEntity> searchUser(String username, String email, String fullname, String dob, String phone, String street, String city, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
        return userRepository.searchUser(username, email, fullname, dob, phone, street,city,pageable);
    }
//    public String getUserRoleById(Integer userId) {
//        // Query để lấy vai trò của người dùng dựa trên userId
//        return userRepository.findRoleByUserId(userId);
//    }

}
