package com.devgaze.uanghabis.service.auth;

import com.devgaze.uanghabis.entity.RefreshToken;
import com.devgaze.uanghabis.entity.User;
import com.devgaze.uanghabis.repository.RefreshTokenRepository;
import com.devgaze.uanghabis.repository.UserRepository;
import com.devgaze.uanghabis.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        RefreshToken refreshToken = new RefreshToken();
        String token = jwtUtils.generateRefreshToken(email);

        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(jwtUtils.getExpirationFromToken(token));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Token is expired, please login again.");
        }

        return token;
    }

    public void deleteByUser (User user) {
        refreshTokenRepository.deleteByUser(user);
    }

}
