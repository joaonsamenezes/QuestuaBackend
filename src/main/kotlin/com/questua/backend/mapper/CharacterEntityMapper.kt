package com.questua.backend.mapper

import com.questua.backend.dto.CharacterEntityRequestDTO
import com.questua.backend.dto.CharacterEntityResponseDTO
import com.questua.backend.model.CharacterEntity

object CharacterEntityMapper {

    fun toResponse(entity: CharacterEntity): CharacterEntityResponseDTO =
        CharacterEntityResponseDTO(
            id = entity.id!!,
            nameCharacter = entity.nameCharacter,
            persona = entity.persona,
            avatarUrl = entity.avatarUrl,
            spriteSheet = entity.spriteSheet,
            voiceUrl = entity.voiceUrl,
            isAiGenerated = entity.isAiGenerated,
            createdAt = entity.createdAt
        )

    fun toEntity(dto: CharacterEntityRequestDTO): CharacterEntity =
        CharacterEntity(
            nameCharacter = dto.nameCharacter,
            persona = dto.persona,
            avatarUrl = dto.avatarUrl,
            spriteSheet = dto.spriteSheet,
            voiceUrl = dto.voiceUrl,
            isAiGenerated = dto.isAiGenerated
        )
}
 