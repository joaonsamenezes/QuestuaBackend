package com.questua.backend.dto
import jakarta.validation.constraints.*
data class GoogleSyncRequestDTO(
    @field:NotBlank(message = "Email não pode ser vazio")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "o nome não pode ser vazio")
    val displayName: String,

    val avatarUrl: String? = null,
    
    val nativeLanguageId: String? = null,

    val cefrLevel: String? = "A1"
)