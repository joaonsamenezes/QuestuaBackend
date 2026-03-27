package com.questua.backend.mapper

import com.questua.backend.dto.AdventurerTierRequestDTO
import com.questua.backend.dto.AdventurerTierResponseDTO
import com.questua.backend.model.AdventurerTier

object AdventurerTierMapper {
    fun toResponse(entity: AdventurerTier): AdventurerTierResponseDTO =
        AdventurerTierResponseDTO(
            id = entity.id!!,
            keyName = entity.keyName,
            nameDisplay = entity.nameDisplay,
            iconUrl = entity.iconUrl,
            colorHex = entity.colorHex,
            orderIndex = entity.orderIndex,
            levelRequired = entity.levelRequired,
            createdAt = entity.createdAt
        )

    fun toEntity(dto: AdventurerTierRequestDTO): AdventurerTier =
        AdventurerTier(
            keyName = dto.keyName,
            nameDisplay = dto.nameDisplay,
            iconUrl = dto.iconUrl,
            colorHex = dto.colorHex,
            orderIndex = dto.orderIndex,
            levelRequired = dto.levelRequired
        )
}