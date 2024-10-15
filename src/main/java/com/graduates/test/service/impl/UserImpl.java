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

    @Override
    public List<UserEntity> getUsersByRole(String roleName) {
            return userRepository. findByRoles_Name(roleName);
        }

    public boolean isAdmin(Integer userId) {
        // Lấy thông tin người dùng từ userId
        UserEntity user = userRepository.findById(userId).orElse(null);

        // Kiểm tra nếu user tồn tại và có role là "ROLE_ADMIN"
        if (user != null && user.getRoles() != null) {
            return user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        }

        return false;
    }
    }



