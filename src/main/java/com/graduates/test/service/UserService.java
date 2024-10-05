package com.graduates.test.service;

import com.graduates.test.model.UserEntity;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserEntity>  searchUser(String username, String  email, String fullname, String dob, String phone, String address, int page, int sizes);
}
