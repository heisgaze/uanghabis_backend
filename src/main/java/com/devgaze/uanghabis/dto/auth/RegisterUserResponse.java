package com.devgaze.uanghabis.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RegisterUserResponse {
    private UUID id;
    private String name;
    private String email;
}
