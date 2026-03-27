package com.questua.backend.dto

import com.questua.backend.model.Persona
import com.questua.backend.model.SpriteSheet
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

data class CharacterEntityRequestDTO(
    @field:NotBlank(message = "nameCharacter não pode ser vazio")
    @field:Size(max = 100, message = "nameCharacter deve ter no máximo 100 caracteres")
    val nameCharacter: String,

    val persona: Persona? = null,

    @field:NotBlank(message = "avatarUrl não pode ser vazio")
    @field:Size(max = 255, message = "avatarUrl deve ter no máximo 255 caracteres")
    val avatarUrl: String,

    val spriteSheet: SpriteSheet? = null,

    @field:Size(max = 255, message = "voiceUrl deve ter no máximo 255 caracteres")
    val voiceUrl: String? = null,

    val isAiGenerated: Boolean = false
)

data class CharacterEntityResponseDTO(
    val id: UUID,
    val nameCharacter: String,
    val persona: Persona?,
    val avatarUrl: String,
    val spriteSheet: SpriteSheet?,
    val voiceUrl: String?,
    val isAiGenerated: Boolean,
    val createdAt: Date
)
 