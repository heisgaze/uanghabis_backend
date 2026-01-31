package com.devgaze.uanghabis.services.auth;

import com.devgaze.uanghabis.dto.auth.LoginUserRequest;
import com.devgaze.uanghabis.dto.auth.LoginUserResponse;
import com.devgaze.uanghabis.dto.auth.RegisterUserRequest;
import com.devgaze.uanghabis.dto.auth.RegisterUserResponse;
import com.devgaze.uanghabis.entity.User;
import com.devgaze.uanghabis.repository.UserRepository;
import com.devgaze.uanghabis.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
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

    @Transactional
    public LoginUserResponse login(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Email or password is wrong"
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Email or password is wrong"
            );
        }

        String token = jwtUtils.generateToken(user);

        return LoginUserResponse.builder()
                .token(token)
                .expiresAt(jwtUtils.getExpirationTime())
                .build();
    }


    }
}
