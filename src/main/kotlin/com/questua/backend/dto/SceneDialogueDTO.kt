package com.questua.backend.dto

import com.questua.backend.model.*
import jakarta.validation.constraints.*
import java.util.*

data class SceneDialogueRequestDTO(
    @field:NotBlank(message = "descriptionDialogue não pode ser vazio")
    val descriptionDialogue: String,

    @field:NotBlank(message = "backgroundUrl não pode ser vazio")
    @field:Size(max = 255, message = "backgroundUrl deve ter no máximo 255 caracteres")
    val backgroundUrl: String,

    @field:Size(max = 255, message = "bgMusicUrl deve ter no máximo 255 caracteres")
    val bgMusicUrl: String? = null,

    val characterStates: List<CharacterState>? = null,
    val sceneEffects: List<SceneEffect>? = null,

    val speakerCharacterId: UUID? = null,

    @field:NotBlank(message = "textContent não pode ser vazio")
    val textContent: String,

    @field:Size(max = 255, message = "audioUrl deve ter no máximo 255 caracteres")
    val audioUrl: String? = null,

    val expectsUserResponse: Boolean = false,

    @field:NotNull(message = "inputMode é obrigatório")
    val inputMode: InputMode,

    val expectedResponse: String? = null,
    val choices: List<Choice>? = null,
    val nextDialogueId: UUID? = null,
    val isAiGenerated: Boolean = false
)

data class SceneDialogueResponseDTO(
    val id: UUID,
    val descriptionDialogue: String,
    val backgroundUrl: String,
    val bgMusicUrl: String?,
    val characterStates: List<CharacterState>?,
    val sceneEffects: List<SceneEffect>?,
    val speakerCharacterId: UUID?,
    val textContent: String,
    val audioUrl: String?,
    val expectsUserResponse: Boolean,
    val inputMode: InputMode,
    val expectedResponse: String?,
    val choices: List<Choice>?,
    val nextDialogueId: UUID?,
    val isAiGenerated: Boolean,
    val createdAt: Date
)
 