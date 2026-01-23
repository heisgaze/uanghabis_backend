package com.devgaze.uanghabis.service;

import com.devgaze.uanghabis.dto.auth.RegisterUserRequest;
import com.devgaze.uanghabis.dto.auth.RegisterUserResponse;
import com.devgaze.uanghabis.entity.User;
import com.devgaze.uanghabis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email already registered");
        }

        if (userRepository.existsByName(request.getName())) {
            throw new RuntimeException("username already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User saved = userRepository.save(user);

        return RegisterUserResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();
    }
}
