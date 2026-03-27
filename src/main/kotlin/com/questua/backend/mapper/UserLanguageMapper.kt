package com.questua.backend.mapper

import com.questua.backend.dto.UserLanguageRequestDTO
import com.questua.backend.dto.UserLanguageResponseDTO
import com.questua.backend.model.UserLanguage

object UserLanguageMapper {

    fun toResponse(entity: UserLanguage): UserLanguageResponseDTO =
        UserLanguageResponseDTO(
            id = entity.id!!,
            userId = entity.userId,
            languageId = entity.languageId,
            statusLanguage = entity.statusLanguage,
            cefrLevel = entity.cefrLevel,
            questsTowardsNextLevel = entity.questsTowardsNextLevel,
            gamificationLevel = entity.gamificationLevel,
            xpTotal = entity.xpTotal,
            streakDays = entity.streakDays,
            unlockedContent = entity.unlockedContent,
            adventurerTierId = entity.adventurerTier?.id,
            startedAt = entity.startedAt,
            lastActiveAt = entity.lastActiveAt
        )

    fun toEntity(dto: UserLanguageRequestDTO): UserLanguage =
        UserLanguage(
            userId = dto.userId,
            languageId = dto.languageId,
            statusLanguage = dto.statusLanguage,
            cefrLevel = dto.cefrLevel,
            questsTowardsNextLevel = dto.questsTowardsNextLevel,
            gamificationLevel = dto.gamificationLevel,
            xpTotal = dto.xpTotal,
            streakDays = dto.streakDays,
            unlockedContent = dto.unlockedContent,
            startedAt = dto.startedAt,
            lastActiveAt = dto.lastActiveAt
        )
}