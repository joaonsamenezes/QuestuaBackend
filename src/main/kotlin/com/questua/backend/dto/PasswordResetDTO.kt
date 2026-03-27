package com.questua.backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ForgotPasswordRequestDTO(
    @field:NotBlank(message = "Email não pode ser vazio")
    @field:Email(message = "Email inválido")
    val email: String
)

data class ResetPasswordRequestDTO(
    @field:NotBlank(message = "Código não pode ser vazio")
    val code: String,

    @field:NotBlank(message = "Nova senha não pode ser vazia")
    val newPassword: String
)