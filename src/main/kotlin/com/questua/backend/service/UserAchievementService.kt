package com.questua.backend.service

import com.questua.backend.model.UserAchievement
import com.questua.backend.repository.UserAchievementRepository 
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserAchievementService(private val repository: UserAchievementRepository) {
 
    fun findAll(pageable: Pageable): Page<UserAchievement> = 
        repository.findAll(pageable)  

    fun findById(id: UUID): UserAchievement =
        repository.findById(id).orElseThrow { EntityNotFoundException("UserAchievement not found: $id") } 

    fun findByUser(userId: UUID, pageable: Pageable): Page<UserAchievement> =
        repository.findByUserId(userId, pageable)

    fun findByUserAndLanguage(userId: UUID, languageId: UUID, pageable: Pageable): Page<UserAchievement> =
        repository.findByUserIdAndLanguageId(userId, languageId, pageable)

    fun create(ua: UserAchievement): UserAchievement =
        repository.save(ua)

    fun update(id: UUID, updated: UserAchievement): UserAchievement {
        val existing = findById(id)
        val merged = existing.copy(
            userId = updated.userId,
            achievementId = updated.achievementId,
            languageId = updated.languageId,
            awardedAt = updated.awardedAt
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("UserAchievement not found: $id")
        repository.deleteById(id)
    }
}
 