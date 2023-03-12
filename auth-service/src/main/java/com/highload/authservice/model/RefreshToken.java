package com.highload.authservice.model;

import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Table
public class RefreshToken {
    @Id
    @GeneratedValue
    @Column(name = "token_id")
    private UUID id;

    @NotBlank(message = "token cannot be empty")
    @Column(name = "token")
    private String token;

    @NotBlank(message = "username cannot be empty")
    @Column(name = "username")
    private String username;

    private Instant expiryDate;
}
