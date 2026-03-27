package com.questua.backend.service

import com.questua.backend.model.Achievement
import com.questua.backend.model.UserAchievement
import com.questua.backend.model.enums.AchievementConditionType
import com.questua.backend.repository.AchievementRepository
import com.questua.backend.repository.UserAchievementRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class AchievementEvaluationService(
    private val achievementRepository: AchievementRepository,
    private val userAchievementRepository: UserAchievementRepository
) {

    fun evaluateCondition(
        userId: UUID,
        conditionType: AchievementConditionType,
        languageId: UUID? = null,
        targetId: UUID? = null,
        currentValue: Int = 1
    ) {
        val eligibleAchievements = if (targetId != null) {
            achievementRepository.findEligibleAchievementsWithTarget(conditionType, targetId, languageId)
        } else {
            achievementRepository.findEligibleAchievements(conditionType, languageId)
        }

        val existingAchievements = userAchievementRepository.findByUserId(userId, Pageable.unpaged())
            .content
            .map { it.achievementId }
            .toSet()

        val achievementsToGrant = eligibleAchievements.filter { achievement ->
            achievement.id !in existingAchievements && currentValue >= achievement.requiredAmount
        }

        grantAchievements(userId, achievementsToGrant)
    }

    private fun grantAchievements(userId: UUID, achievements: List<Achievement>) {
        if (achievements.isEmpty()) return

        val newGrants = achievements.map { achievement ->
            UserAchievement(
                userId = userId,
                achievementId = achievement.id!!,
                languageId = achievement.languageId,
                awardedAt = Date()
            )
        }

        userAchievementRepository.saveAll(newGrants)
    }
}