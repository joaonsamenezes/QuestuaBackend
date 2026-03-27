package com.questua.backend.mapper

import com.questua.backend.dto.SceneDialogueRequestDTO
import com.questua.backend.dto.SceneDialogueResponseDTO
import com.questua.backend.model.SceneDialogue

object SceneDialogueMapper {

    fun toResponse(entity: SceneDialogue): SceneDialogueResponseDTO =
        SceneDialogueResponseDTO(
            id = entity.id!!,
            descriptionDialogue = entity.descriptionDialogue,
            backgroundUrl = entity.backgroundUrl,
            bgMusicUrl = entity.bgMusicUrl,
            characterStates = entity.characterStates,
            sceneEffects = entity.sceneEffects,
            speakerCharacterId = entity.speakerCharacterId,
            textContent = entity.textContent,
            audioUrl = entity.audioUrl,
            expectsUserResponse = entity.expectsUserResponse,
            inputMode = entity.inputMode,
            expectedResponse = entity.expectedResponse,
            choices = entity.choices,
            nextDialogueId = entity.nextDialogueId,
            isAiGenerated = entity.isAiGenerated,
            createdAt = entity.createdAt
        )

    fun toEntity(dto: SceneDialogueRequestDTO): SceneDialogue =
        SceneDialogue(
            descriptionDialogue = dto.descriptionDialogue,
            backgroundUrl = dto.backgroundUrl,
            bgMusicUrl = dto.bgMusicUrl,
            characterStates = dto.characterStates,
            sceneEffects = dto.sceneEffects,
            speakerCharacterId = dto.speakerCharacterId,
            textContent = dto.textContent,
            audioUrl = dto.audioUrl,
            expectsUserResponse = dto.expectsUserResponse,
            inputMode = dto.inputMode,
            expectedResponse = dto.expectedResponse,
            choices = dto.choices,
            nextDialogueId = dto.nextDialogueId,
            isAiGenerated = dto.isAiGenerated
        )
}
