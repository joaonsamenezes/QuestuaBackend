package com.questua.backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

data class LanguageRequestDTO(
    @field:NotBlank(message = "codeLanguage não pode ser vazio")
    @field:Size(max = 10, message = "codeLanguage deve ter no máximo 10 caracteres")
    val codeLanguage: String,

    @field:NotBlank(message = "nameLanguage não pode ser vazio")
    @field:Size(max = 100, message = "nameLanguage deve ter no máximo 100 caracteres")
    val nameLanguage: String,

    @field:Size(max = 255, message = "iconUrl deve ter no máximo 255 caracteres")
    val iconUrl: String? = null
)

data class LanguageResponseDTO(
    val id: UUID,
    val codeLanguage: String,
    val nameLanguage: String,
    val iconUrl: String?
)
 