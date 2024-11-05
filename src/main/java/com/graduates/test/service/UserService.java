package com.graduates.test.service;

import com.graduates.test.dto.UpdateUserRequest;
import com.graduates.test.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<String> getRolesByUserId(Integer userId);
    Page<UserEntity>  searchUser(String username, String  email, String fullname, String dob, String phone,String street, String city, int page, int sizes);

    List<UserEntity> getUsersByRole(String roleName);
    boolean isAdmin(Integer userId);

    String updateUser(Integer userId, UpdateUserRequest updateUserRequest);

    String saveImage(MultipartFile file);
    UserEntity getUser(Integer idUser);
    String updateAccount(int idUser, String fullname,String email, String dob, MultipartFile file);
}
