package com.graduates.test.service;

import com.graduates.test.model.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    List<String> getRolesByUserId(Integer userId);
    Page<UserEntity>  searchUser(String username, String  email, String fullname, String dob, String phone,String street, String city, int page, int sizes);

    List<UserEntity> getUsersByRole(String roleName);
}
