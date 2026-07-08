package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    @NotBlank @Email(message = "Email inválido")
    String email,

    @NotBlank @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    String password
) {}
