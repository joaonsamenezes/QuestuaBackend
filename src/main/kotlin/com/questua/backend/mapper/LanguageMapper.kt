package com.questua.backend.mapper

import com.questua.backend.dto.LanguageRequestDTO
import com.questua.backend.dto.LanguageResponseDTO
import com.questua.backend.model.Language

object LanguageMapper {

    fun toResponse(entity: Language): LanguageResponseDTO =
        LanguageResponseDTO(
            id = entity.id!!,
            codeLanguage = entity.codeLanguage,
            nameLanguage = entity.nameLanguage,
            iconUrl = entity.iconUrl
        )

    fun toEntity(dto: LanguageRequestDTO): Language =
        Language(
            codeLanguage = dto.codeLanguage,
            nameLanguage = dto.nameLanguage,
            iconUrl = dto.iconUrl
        )
}
