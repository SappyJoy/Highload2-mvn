package com.highload.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponse {
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String newJWTToken;
}
