package com.questua.backend.mapper

import com.questua.backend.dto.QuestPointRequestDTO
import com.questua.backend.dto.QuestPointResponseDTO
import com.questua.backend.model.QuestPoint

object QuestPointMapper {
    fun toEntity(dto: QuestPointRequestDTO): QuestPoint = QuestPoint(
        cityId = dto.cityId,
        title = dto.title,
        descriptionQpoint = dto.descriptionQpoint,
        difficulty = dto.difficulty,
        lat = dto.lat,
        lon = dto.lon,
        imageUrl = dto.imageUrl,
        iconUrl = dto.iconUrl,
        unlockRequirement = dto.unlockRequirement,
        isPremium = dto.isPremium,
        isAiGenerated = dto.isAiGenerated,
        isPublished = dto.isPublished
    )

    fun toResponse(entity: QuestPoint): QuestPointResponseDTO = QuestPointResponseDTO( 
        id = entity.id!!,
        cityId = entity.cityId,
        title = entity.title,
        descriptionQpoint = entity.descriptionQpoint,
        difficulty = entity.difficulty,
        lat = entity.lat,
        lon = entity.lon,
        imageUrl = entity.imageUrl,
        iconUrl = entity.iconUrl,
        unlockRequirement = entity.unlockRequirement,
        isPremium = entity.isPremium,
        isAiGenerated = entity.isAiGenerated,
        isPublished = entity.isPublished,
        createdAt = entity.createdAt
    )
} 