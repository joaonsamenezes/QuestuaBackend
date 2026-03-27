package com.questua.backend.dto

import jakarta.validation.constraints.*

data class LoginRequestDTO(
    @field:NotBlank(message = "Email não pode ser vazio")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "Senha não pode ser vazia")
    val password: String
)

data class LoginResponseDTO(
    val token: String
)

data class RegisterRequestDTO(
    @field:NotBlank(message = "Email não pode ser vazio")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "Senha não pode ser vazia")
    val password: String,

    @field:NotBlank(message = "o nome não pode ser vazio")
    @field:Size(max = 100, message = "o nome deve ter no máximo 100 caracteres")
    val displayName: String,

    @field:Size(max = 255, message = "avatarUrl deve ter no máximo 255 caracteres")
    val avatarUrl: String? = null,

    @field:NotBlank(message = "linguagem nativa é obrigatória")
    val nativeLanguageId: String,

    val cefrLevel: String? = "A1"
)

data class VerifyEmailRequestDTO(
    @field:NotBlank(message = "Email não pode ser vazio")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "Código não pode ser vazio")
    val code: String
)