package com.questua.backend.dto

import jakarta.validation.constraints.NotNull
import java.util.*

data class UserAchievementRequestDTO(
    @field:NotNull(message = "userId é obrigatório")
    val userId: UUID,

    @field:NotNull(message = "achievementId é obrigatório")
    val achievementId: UUID,

    val languageId: UUID? = null
)

data class UserAchievementResponseDTO(
    val id: UUID,
    val userId: UUID,
    val achievementId: UUID,
    val languageId: UUID?,
    val awardedAt: Date
)
 