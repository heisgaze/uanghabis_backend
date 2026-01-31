package com.devgaze.uanghabis.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class LoginUserResponse {
    private String token;
    private String refreshToken;
    private Instant expiresAt;
    private Instant refreshTokenExpiresAt;
}
