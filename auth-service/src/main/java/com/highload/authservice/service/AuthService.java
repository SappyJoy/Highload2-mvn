package com.highload.authservice.service;

import com.highload.authservice.dto.LoginRequestDto;
import com.highload.authservice.dto.LoginResponseDto;
import com.highload.authservice.exception.AuthenticationException;
import com.highload.authservice.security.JwtProvider;
import com.highload.feign.dto.UserDto;
import com.highload.feign.model.User;
import com.highload.feign.reactive_client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UserClient userClient;

    public Mono<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        return userClient.authenticate(loginRequestDto.getUsername())
                .onErrorResume(e -> Mono.error(new AuthenticationException("Error in userClient: " + e.getMessage())))
                .handle((UserDto userDto, SynchronousSink<UserDto> sink) -> {
                    if (!userDto.getPassword().equals(loginRequestDto.getPassword())) {
                        sink.error(new AuthenticationException("Password doesn't match"));
                    } else {
                        sink.next(userDto);
                    }
                })
                .map(userDto -> new LoginResponseDto(jwtProvider.generateJwtToken(userDto)))
                .onErrorResume(e -> Mono.error(new AuthenticationException("Error while generating LoginResponse: " + e.getMessage())));
    }

    public Mono<LoginResponseDto> signUpUser(UserDto userDto) {
        return userClient.createUser(userDto)
                .onErrorResume(e -> Mono.error(new AuthenticationException("Error in userClient: " + e.getMessage())))
                .map(userDto1 -> new LoginResponseDto(jwtProvider.generateJwtToken(userDto1)))
                .onErrorResume(e -> Mono.error(new AuthenticationException("Error while generating LoginResponse: " + e.getMessage())));
    }

    public Mono<UserDto> getUserByUsername(String username) {
        return userClient.authenticate(username)
                .onErrorResume(e -> Mono.error(new AuthenticationException("Error in userClient: " + e.getMessage())));
    }
}
