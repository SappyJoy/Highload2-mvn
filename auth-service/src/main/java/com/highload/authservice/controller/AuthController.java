package com.highload.authservice.controller;

import com.highload.authservice.dto.LoginResponseDto;
import com.highload.authservice.dto.RefreshTokenResponse;
import com.highload.authservice.exception.RefreshTokenException;
import com.highload.authservice.model.RefreshToken;
import com.highload.authservice.security.JwtProvider;
import com.highload.authservice.service.AuthService;
import com.highload.authservice.dto.LoginRequestDto;
import com.highload.authservice.service.RefreshTokenService;
import com.highload.feign.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public Mono<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/sign_up")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public Mono<LoginResponseDto> signUp(@Valid @RequestBody UserDto userDto) {
        return authService.signUpUser(userDto);
    }

    @PostMapping("/parseToken")
    Mono<UserDto> parseToken(@RequestBody String token) {
        jwtProvider.validateJwtToken(token);
        String username = jwtProvider.getUsername(token);

        return authService.getUserByUsername(username);
    }

    @PostMapping("/refreshToken")
    public Mono<ResponseEntity<RefreshTokenResponse>> refreshToken(@RequestBody String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .next()
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsername)
                .flatMap(authService::getUserByUsername)
                .map(userDto -> ResponseEntity.ok(new RefreshTokenResponse(refreshToken, jwtProvider.generateJwtToken(userDto))))
                .onErrorResume(e -> Mono.error(new RefreshTokenException("Error while refreshing refreshToken: " + e.getMessage())));
    }
}
