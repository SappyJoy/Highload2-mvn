package com.highload.authservice.security;

import com.highload.authservice.exception.UserExistException;
import com.highload.feign.dto.UserDto;
import com.highload.feign.model.User;
import com.highload.feign.reactive_client.UserClient;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final UserClient userClient;
    @Value("${jwt.token.secret}")
    private String jwtSecret;

    public String generateJwtToken(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = Date.from(now.toInstant(ZoneOffset.UTC));
        Date expiration = Date.from(now.plusHours(1).toInstant(ZoneOffset.UTC));
        User user = userClient.getUserById(userId);
        Map<String, Object> claims = new HashMap<>();

        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());
        return Jwts
                .builder()
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UUID getUserIdFromJwtToken(String token) {
        return UUID.fromString(
                Jwts
                        .parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
