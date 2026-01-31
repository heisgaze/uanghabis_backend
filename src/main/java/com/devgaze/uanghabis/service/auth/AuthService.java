package com.devgaze.uanghabis.service.auth;

import com.devgaze.uanghabis.dto.auth.LoginUserRequest;
import com.devgaze.uanghabis.dto.auth.LoginUserResponse;
import com.devgaze.uanghabis.dto.auth.RegisterUserRequest;
import com.devgaze.uanghabis.dto.auth.RegisterUserResponse;
import com.devgaze.uanghabis.entity.RefreshToken;
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
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already registered");
        }

        if (userRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered");
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

        refreshTokenService.deleteByUser(user);
        String token = jwtUtils.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());

        return LoginUserResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .expiresAt(jwtUtils.getExpirationFromToken(token))
                .refreshTokenExpiresAt(refreshToken.getExpiryDate())
                .build();
    }
}
