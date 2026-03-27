package com.questua.backend.mapper

import com.questua.backend.dto.AchievementRequestDTO
import com.questua.backend.dto.AchievementResponseDTO
import com.questua.backend.model.Achievement
import java.util.UUID

object AchievementMapper {
    fun toEntity(dto: AchievementRequestDTO): Achievement =
        Achievement(
            keyName = dto.keyName,
            nameAchievement = dto.nameAchievement,
            descriptionAchievement = dto.descriptionAchievement,
            iconUrl = dto.iconUrl,
            xpReward = dto.xpReward,
            rarity = dto.rarity,
            isHidden = dto.isHidden,
            category = dto.category,
            isGlobal = dto.isGlobal,
            languageId = dto.languageId,
            conditionType = dto.conditionType,
            targetId = dto.targetId,
            requiredAmount = dto.requiredAmount,
            metadata = dto.metadata
        )

    fun toResponse(entity: Achievement): AchievementResponseDTO =
        AchievementResponseDTO(
            id = entity.id ?: UUID.randomUUID(),
            keyName = entity.keyName,
            nameAchievement = entity.nameAchievement,
            descriptionAchievement = entity.descriptionAchievement,
            iconUrl = entity.iconUrl,
            xpReward = entity.xpReward,
            rarity = entity.rarity,
            isHidden = entity.isHidden,
            category = entity.category,
            isGlobal = entity.isGlobal,
            languageId = entity.languageId,
            conditionType = entity.conditionType,
            targetId = entity.targetId,
            requiredAmount = entity.requiredAmount,
            metadata = entity.metadata
        )
}