package com.highload.authservice.service;

import com.highload.authservice.exception.RefreshTokenException;
import com.highload.authservice.model.RefreshToken;
import com.highload.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("{refresh.token.duration.md")
    private final Long refreshTokenDurationMs;

    public RefreshToken createRefreshToken(String username) {
        return new RefreshToken(null, UUID.randomUUID().toString(), username, Instant.now().plusMillis(refreshTokenDurationMs));
    }

    public Flux<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    @SneakyThrows
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token).doOnNext(x -> { System.out.println(x + " were deleted"); }).block();
            throw new RefreshTokenException(token.getToken() + ": refresh token has been expired. Please login again");
        }
        return token;
    }
}
