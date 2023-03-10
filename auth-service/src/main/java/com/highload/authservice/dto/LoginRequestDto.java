package com.highload.authservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
