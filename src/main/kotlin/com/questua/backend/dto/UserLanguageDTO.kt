package com.questua.backend.dto

import com.questua.backend.model.StatusLanguage
import com.questua.backend.model.UnlockedContent
import jakarta.validation.constraints.*
import java.util.*

data class UserLanguageRequestDTO(
    @field:NotNull(message = "userId é obrigatório")
    val userId: UUID,

    @field:NotNull(message = "languageId é obrigatório")
    val languageId: UUID,

    @field:NotNull(message = "statusLanguage é obrigatório")
    val statusLanguage: StatusLanguage = StatusLanguage.ACTIVE,

    @field:NotBlank(message = "cefrLevel não pode ser vazio")
    @field:Size(max = 2, message = "cefrLevel deve ter no máximo 2 caracteres")
    val cefrLevel: String = "A1",

    @field:Min(value = 0, message = "questsTowardsNextLevel não pode ser negativo")
    val questsTowardsNextLevel: Int = 0,

    @field:Min(value = 1, message = "gamificationLevel deve ser >= 1")
    val gamificationLevel: Int = 1,

    @field:Min(value = 0, message = "xpTotal não pode ser negativo")
    val xpTotal: Int = 0,

    @field:Min(value = 0, message = "streakDays não pode ser negativo")
    val streakDays: Int = 0,

    val unlockedContent: UnlockedContent? = null,

    val adventurerTierId: UUID? = null,

    @field:NotNull(message = "startedAt é obrigatório")
    val startedAt: Date = Date(),

    val lastActiveAt: Date? = null
)

data class UserLanguageResponseDTO(
    val id: UUID,
    val userId: UUID,
    val languageId: UUID,
    val statusLanguage: StatusLanguage,
    val cefrLevel: String,
    val questsTowardsNextLevel: Int,
    val gamificationLevel: Int,
    val xpTotal: Int,
    val streakDays: Int,
    val unlockedContent: UnlockedContent?,
    val adventurerTierId: UUID?,
    val startedAt: Date,
    val lastActiveAt: Date?
)