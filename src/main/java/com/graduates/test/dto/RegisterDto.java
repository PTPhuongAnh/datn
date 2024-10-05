package com.graduates.test.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private  String username;
    private  String password;
    private String email;
    private String fullname;
    private String dob;
    private String phone;
    private String address;
}
