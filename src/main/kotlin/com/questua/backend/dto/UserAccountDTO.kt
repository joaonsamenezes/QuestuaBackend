package com.questua.backend.dto

import com.questua.backend.model.UserRole
import jakarta.validation.constraints.*
import java.util.*
import java.time.Instant

data class UserAccountRequestDTO(
    @field:NotBlank(message = "Email não pode ser vazio")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "displayName não pode ser vazio")
    @field:Size(max = 100, message = "displayName deve ter no máximo 100 caracteres")
    val displayName: String,

    val password: String?,

    @field:Size(max = 255, message = "avatarUrl deve ter no máximo 255 caracteres")
    val avatarUrl: String? = null,

    @field:NotNull(message = "nativeLanguageId é obrigatório")
    val nativeLanguageId: UUID,

    @field:NotNull(message = "userRole é obrigatório")
    val userRole: UserRole = UserRole.USER
)

data class UserAccountResponseDTO(
    val id: UUID,
    val email: String,
    val displayName: String,
    val avatarUrl: String?,
    val nativeLanguageId: UUID,
    val userRole: UserRole,
    val createdAt: Instant,
    val lastActiveAt: Instant?
)
 