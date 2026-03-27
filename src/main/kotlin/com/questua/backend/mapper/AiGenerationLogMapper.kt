package com.questua.backend.mapper

import com.questua.backend.dto.AiGenerationLogRequestDTO
import com.questua.backend.dto.AiGenerationLogResponseDTO
import com.questua.backend.model.AiGenerationLog

object AiGenerationLogMapper {

    fun toResponse(entity: AiGenerationLog): AiGenerationLogResponseDTO =
        AiGenerationLogResponseDTO(
            id = entity.id!!,
            userId = entity.userId,
            targetType = entity.targetType,
            targetId = entity.targetId,
            prompt = entity.prompt,
            modelName = entity.modelName,
            responseText = entity.responseText,
            responseMeta = entity.responseMeta,
            statusGeneration = entity.statusGeneration,
            createdAt = entity.createdAt
        )

    fun toEntity(dto: AiGenerationLogRequestDTO): AiGenerationLog =
        AiGenerationLog(
            userId = dto.userId,
            targetType = dto.targetType,
            targetId = dto.targetId,
            prompt = dto.prompt,
            modelName = dto.modelName,
            responseText = dto.responseText,
            responseMeta = dto.responseMeta,
            statusGeneration = dto.statusGeneration
        )
}
