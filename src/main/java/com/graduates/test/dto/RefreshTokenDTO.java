package com.graduates.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RefreshTokenDTO {
    private String token;
}
