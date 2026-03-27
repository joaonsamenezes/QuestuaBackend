package com.questua.backend.dto

import com.questua.backend.model.LearningFocus
import com.questua.backend.model.UnlockRequirement
import jakarta.validation.constraints.*
import java.util.*

data class QuestRequestDTO(
    @field:NotNull(message = "questPointId é obrigatório")
    val questPointId: UUID,

    val firstDialogueId: UUID? = null,

    @field:NotBlank(message = "title não pode ser vazio")
    @field:Size(max = 150, message = "title deve ter no máximo 150 caracteres")
    val title: String,

    @field:NotBlank(message = "descriptionQuest não pode ser vazio")
    val descriptionQuest: String,

    @field:Min(value = 1, message = "difficulty deve ser >= 1")
    val difficulty: Short = 1,

    @field:Min(value = 1, message = "orderIndex deve ser >= 1")
    val orderIndex: Short = 1,

    @field:Min(value = 0, message = "xpValue não pode ser negativo")
    val xpValue: Int = 0,

    @field:Min(value = 1, message = "xpPerQuestion deve ser >= 1")
    val xpPerQuestion: Int = 10,

    val unlockRequirement: UnlockRequirement? = null,
    val learningFocus: LearningFocus? = null,
    val isPremium: Boolean = false,
    val isAiGenerated: Boolean = false,
    val isPublished: Boolean = false
)

data class QuestResponseDTO(
    val id: UUID,
    val questPointId: UUID,
    val firstDialogueId: UUID?,
    val title: String,
    val descriptionQuest: String?,
    val difficulty: Short,
    val orderIndex: Short,
    val xpValue: Int,
    val xpPerQuestion: Int,
    val unlockRequirement: UnlockRequirement?,
    val learningFocus: LearningFocus?,
    val isPremium: Boolean,
    val isAiGenerated: Boolean,
    val isPublished: Boolean,
    val createdAt: Date,
    val isLocked: Boolean = false,
    val lockMessage: String? = null
)