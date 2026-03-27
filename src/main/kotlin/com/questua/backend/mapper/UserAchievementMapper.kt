package com.questua.backend.mapper

import com.questua.backend.dto.UserAchievementRequestDTO
import com.questua.backend.dto.UserAchievementResponseDTO
import com.questua.backend.model.UserAchievement

object UserAchievementMapper {

    fun toResponse(entity: UserAchievement): UserAchievementResponseDTO =
        UserAchievementResponseDTO(
            id = entity.id!!,
            userId = entity.userId,
            achievementId = entity.achievementId,
            languageId = entity.languageId,
            awardedAt = entity.awardedAt
        )

    fun toEntity(dto: UserAchievementRequestDTO): UserAchievement =
        UserAchievement(
            userId = dto.userId,
            achievementId = dto.achievementId,
            languageId = dto.languageId
        )
}
