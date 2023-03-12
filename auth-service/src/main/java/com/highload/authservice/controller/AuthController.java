package com.highload.authservice.controller;

import com.highload.authservice.security.JwtProvider;
import com.highload.authservice.service.AuthService;
import com.highload.authservice.dto.LoginRequestDto;
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

//    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String response = authService.login(loginRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/sign_up")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDto userDto) {
        authService.signUpUser(userDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User created!");
    }

}
