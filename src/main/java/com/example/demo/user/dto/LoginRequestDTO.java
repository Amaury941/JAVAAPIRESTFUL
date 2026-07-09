package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record LoginRequestDTO(
    @NotBlank @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    String password
) {}