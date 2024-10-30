package com.graduates.test.dto;

public class UpdateUserRequest {
private String username;
    private String email; // Email người dùng
    private String phone; // Số điện thoại người dùng
    private String dob; // Địa chỉ người dùng

    public UpdateUserRequest(String username, String email, String phone, String dob) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }

    public UpdateUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
