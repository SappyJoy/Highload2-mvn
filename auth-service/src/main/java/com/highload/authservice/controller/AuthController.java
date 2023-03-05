package com.highload.authservice.controller;

import com.highload.authservice.security.JwtProvider;
import com.highload.authservice.service.AuthService;
import com.highload.authservice.dto.LoginRequestDto;
import com.highload.shared_lib.feign.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public Mono<LoginRequestDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
//        return authService.login();
        return null;
    }

    @PostMapping("/sign_up")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDto userDto) {
//        return authService.signUpUser(userDto);
        return null;
    }

}
