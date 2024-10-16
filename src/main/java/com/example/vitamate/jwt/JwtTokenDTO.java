package com.example.vitamate.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
