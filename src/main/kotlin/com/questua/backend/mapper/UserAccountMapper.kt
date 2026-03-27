package com.questua.backend.mapper

import com.questua.backend.dto.UserAccountResponseDTO
import com.questua.backend.model.UserAccount

object UserAccountMapper {

    fun toResponse(entity: UserAccount): UserAccountResponseDTO =
        UserAccountResponseDTO(
            id = entity.id!!,
            email = entity.email,
            displayName = entity.displayName,
            avatarUrl = entity.avatarUrl,
            nativeLanguageId = entity.nativeLanguage.id!!,
            userRole = entity.userRole,
            createdAt = entity.createdAt,
            lastActiveAt = entity.lastActiveAt
        )
}
