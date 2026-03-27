package com.questua.backend.dto

import com.questua.backend.model.AiTargetType
import com.questua.backend.model.AiGenerationStatus
import com.questua.backend.model.AiGenerationResponseMeta
import jakarta.validation.constraints.*
import java.util.*

data class AiGenerationLogRequestDTO(
    val userId: UUID? = null,

    @field:NotNull(message = "targetType é obrigatório")
    val targetType: AiTargetType,

    val targetId: UUID? = null,

    @field:NotBlank(message = "prompt não pode ser vazio")
    val prompt: String,

    @field:NotBlank(message = "o nome do modelo não pode ser vazio")
    @field:Size(max = 100, message = "o nome do modelo deve ter no máximo 100 caracteres")
    val modelName: String,

    val responseText: String? = null,
    val responseMeta: AiGenerationResponseMeta? = null,

    @field:NotNull(message = "statusGeneration é obrigatório")
    val statusGeneration: AiGenerationStatus
)

data class AiGenerationLogResponseDTO(
    val id: UUID,
    val userId: UUID? = null,
    val targetType: AiTargetType,
    val targetId: UUID? = null,
    val prompt: String,
    val modelName: String,
    val responseText: String? = null,
    val responseMeta: AiGenerationResponseMeta? = null,
    val statusGeneration: AiGenerationStatus,
    val createdAt: Date
)
