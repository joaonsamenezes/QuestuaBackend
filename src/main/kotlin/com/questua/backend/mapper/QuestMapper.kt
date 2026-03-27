package com.questua.backend.mapper

import com.questua.backend.dto.QuestRequestDTO
import com.questua.backend.dto.QuestResponseDTO
import com.questua.backend.model.Quest

object QuestMapper {
    fun toEntity(dto: QuestRequestDTO): Quest = Quest(
        questPointId = dto.questPointId,
        firstDialogueId = dto.firstDialogueId,
        title = dto.title,
        descriptionQuest = dto.descriptionQuest,
        difficulty = dto.difficulty,
        orderIndex = dto.orderIndex,
        xpValue = dto.xpValue,
        xpPerQuestion = dto.xpPerQuestion,
        unlockRequirement = dto.unlockRequirement,
        learningFocus = dto.learningFocus,
        isPremium = dto.isPremium,
        isAiGenerated = dto.isAiGenerated,
        isPublished = dto.isPublished
    )

    fun toResponse(entity: Quest): QuestResponseDTO = QuestResponseDTO(
        id = entity.id!!,
        questPointId = entity.questPointId,
        firstDialogueId = entity.firstDialogueId,
        title = entity.title,
        descriptionQuest = entity.descriptionQuest,
        difficulty = entity.difficulty,
        orderIndex = entity.orderIndex,
        xpValue = entity.xpValue,
        xpPerQuestion = entity.xpPerQuestion,
        unlockRequirement = entity.unlockRequirement,
        learningFocus = entity.learningFocus,
        isPremium = entity.isPremium,
        isAiGenerated = entity.isAiGenerated,
        isPublished = entity.isPublished,
        createdAt = entity.createdAt
    ) 
}