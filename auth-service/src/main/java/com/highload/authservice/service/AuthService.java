package com.highload.authservice.service;

import com.highload.authservice.dto.LoginRequestDto;
import com.highload.authservice.security.JwtProvider;
import com.highload.feign.dto.UserDto;
import com.highload.feign.model.User;
import com.highload.feign.reactive_client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UserClient userClient;

    public String login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        return jwtProvider.generateJwtToken(user.getUserId());
    }

    public Optional<UserDto> signUpUser(UserDto userDto) {
        return userClient.(userDto);
    }
}
