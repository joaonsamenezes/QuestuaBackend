package com.questua.backend.dto

import com.questua.backend.model.AchievementMetadata
import com.questua.backend.model.enums.AchievementConditionType
import com.questua.backend.model.enums.RarityType
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class AchievementRequestDTO(
    val keyName: String? = null,

    @field:NotBlank(message = "nameAchievement é obrigatório")
    val nameAchievement: String,

    @field:NotBlank(message = "descriptionAchievement é obrigatório")
    val descriptionAchievement: String,

    val iconUrl: String? = null,

    @field:Min(value = 0, message = "xpReward não pode ser negativo")
    val xpReward: Int = 0,

    @field:NotNull(message = "rarity é obrigatória")
    val rarity: RarityType = RarityType.COMMON,

    @field:NotNull(message = "isHidden é obrigatório")
    val isHidden: Boolean = false,

    val category: String? = null,

    @field:NotNull(message = "isGlobal é obrigatório")
    val isGlobal: Boolean = true,

    val languageId: UUID? = null,

    @field:NotNull(message = "conditionType é obrigatório")
    val conditionType: AchievementConditionType,

    val targetId: UUID? = null,

    @field:Min(value = 1, message = "requiredAmount deve ser pelo menos 1")
    val requiredAmount: Int = 1,

    val metadata: AchievementMetadata? = null
)

data class AchievementResponseDTO(
    val id: UUID,
    val keyName: String?,
    val nameAchievement: String,
    val descriptionAchievement: String,
    val iconUrl: String?,
    val xpReward: Int,
    val rarity: RarityType,
    val isHidden: Boolean,
    val category: String?,
    val isGlobal: Boolean,
    val languageId: UUID?,
    val conditionType: AchievementConditionType,
    val targetId: UUID?,
    val requiredAmount: Int,
    val metadata: AchievementMetadata?
)