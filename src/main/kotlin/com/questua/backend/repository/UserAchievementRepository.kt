package com.questua.backend.repository

import com.questua.backend.model.UserAchievement
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserAchievementRepository : JpaRepository<UserAchievement, UUID> {

    fun findByUserId(userId: UUID, pageable: Pageable): Page<UserAchievement>

    fun findByAchievementId(achievementId: UUID, pageable: Pageable): Page<UserAchievement>

    fun findByUserIdAndLanguageId(
        userId: UUID,
        languageId: UUID,
        pageable: Pageable
    ): Page<UserAchievement>
    
    fun existsByUserIdAndAchievementIdAndLanguageId(userId: UUID, achievementId: UUID, languageId: UUID): Boolean
    fun existsByUserIdAndAchievementId(userId: UUID, achievementId: UUID): Boolean
}
 