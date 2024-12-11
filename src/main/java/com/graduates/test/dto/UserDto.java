package com.graduates.test.dto;

import com.graduates.test.model.Address;
import com.graduates.test.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private  Integer idUser;
    private String username;
    private String email;
    private String fullname;
    private String dob;
    private String phone;
    private String address;
    private List<Role> roles;
}
