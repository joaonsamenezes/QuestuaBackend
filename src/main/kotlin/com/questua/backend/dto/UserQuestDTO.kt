package com.questua.backend.dto

import com.questua.backend.model.Response
import com.questua.backend.model.SkillAssessment
import com.questua.backend.model.enums.ProgressStatus
import jakarta.validation.constraints.*
import java.util.*

data class UserQuestRequestDTO(
    @field:NotNull(message = "userId é obrigatório")
    val userId: UUID,

    @field:NotNull(message = "questId é obrigatório")
    val questId: UUID,

    @field:NotNull(message = "progressStatus é obrigatório")
    val progressStatus: ProgressStatus,

    @field:Min(value = 0, message = "xpEarned não pode ser negativo")
    val xpEarned: Int,

    @field:Min(value = 0, message = "score não pode ser negativo")
    val score: Int,

    @field:DecimalMin(value = "0.0", message = "percentComplete não pode ser negativo")
    @field:DecimalMax(value = "100.0", message = "percentComplete não pode ser maior que 100")
    val percentComplete: Double,

    @field:NotNull(message = "lastDialogueId é obrigatório")
    val lastDialogueId: UUID,

    @field:NotNull(message = "lastActivityAt é obrigatório")
    val lastActivityAt: Date,

    val completedAt: Date? = null,
    val responses: List<Response>? = null,
    val overallAssessment: List<SkillAssessment>? = null,
    val startedAt: Date? = null,
)

data class UserQuestResponseDTO(  
    val id: UUID,
    val userId: UUID,
    val questId: UUID,
    val progressStatus: ProgressStatus,
    val xpEarned: Int,
    val score: Int,
    val percentComplete: Double,
    val lastDialogueId: UUID?,
    val lastActivityAt: Date?,
    val completedAt: Date?,
    val responses: List<Response>?,
    val overallAssessment: List<SkillAssessment>?,
    val startedAt: Date?,
)

data class SubmitResponseRequestDTO(
    @field:NotNull(message = "dialogueId é obrigatório")
    val dialogueId: UUID,
    
    @field:NotBlank(message = "A resposta não pode estar vazia")
    val answer: String
)

data class SubmitResponseResultDTO(
    val correct: Boolean,
    val feedback: String?,
    val nextDialogueId: UUID?,
    val xpEarned: Int,
    val isQuestCompleted: Boolean,
    val userQuest: UserQuestResponseDTO
)