package com.questua.backend.mapper

import com.questua.backend.dto.UserQuestRequestDTO
import com.questua.backend.dto.UserQuestResponseDTO
import com.questua.backend.model.UserQuest
import java.util.Date
import java.util.UUID

object UserQuestMapper {
    fun toEntity(dto: UserQuestRequestDTO): UserQuest =
        UserQuest(
            userId = dto.userId,
            questId = dto.questId,
            lastDialogueId = dto.lastDialogueId,
            progressStatus = dto.progressStatus,
            xpEarned = dto.xpEarned,
            score = dto.score,
            percentComplete = dto.percentComplete,
            responses = dto.responses,
            overallAssessment = dto.overallAssessment,
            startedAt = dto.startedAt ?: Date(),
            completedAt = dto.completedAt,
            lastActivityAt = dto.lastActivityAt ?: Date()
        )

    fun toResponse(entity: UserQuest): UserQuestResponseDTO =
        UserQuestResponseDTO(
            id = entity.id ?: UUID.randomUUID(),
            userId = entity.userId,
            questId = entity.questId,
            lastDialogueId = entity.lastDialogueId,
            progressStatus = entity.progressStatus,
            xpEarned = entity.xpEarned,
            score = entity.score ?: 0,
            percentComplete = entity.percentComplete,
            responses = entity.responses,
            overallAssessment = entity.overallAssessment,
            startedAt = entity.startedAt,
            completedAt = entity.completedAt,
            lastActivityAt = entity.lastActivityAt
        )
}