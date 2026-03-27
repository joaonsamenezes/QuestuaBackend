package com.questua.backend.listener

import com.questua.backend.event.*
import com.questua.backend.model.enums.AchievementConditionType
import com.questua.backend.service.AchievementEvaluationService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AchievementEventListener(
    private val evaluationService: AchievementEvaluationService
) {

    @Async
    @EventListener
    fun handleQuestCompleted(event: QuestCompletedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.COMPLETE_SPECIFIC_QUEST, event.languageId, event.questId, 1)
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.COMPLETE_QUEST_AMOUNT, event.languageId, null, 1)
        
        if (event.isPerfect) {
            evaluationService.evaluateCondition(event.userId, AchievementConditionType.PERFECT_QUEST_COMPLETION, event.languageId, event.questId, 1)
        }
        
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.FAST_QUEST_COMPLETION, event.languageId, event.questId, event.durationSeconds.toInt())
    }

    @Async
    @EventListener
    fun handleCityUnlocked(event: CityUnlockedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.UNLOCK_CITY_AMOUNT, event.languageId, null, 1)
    }

    @Async
    @EventListener
    fun handleLevelUp(event: LevelUpEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.REACH_GAMIFICATION_LEVEL, null, null, event.newLevel)
    }

    @Async
    @EventListener
    fun handleXpEarned(event: XpEarnedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.EARN_XP_AMOUNT, null, null, event.totalXp)
    }

    @Async
    @EventListener
    fun handleQuestPointUnlocked(event: QuestPointUnlockedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.UNLOCK_QUEST_POINT_AMOUNT, event.languageId, null, 1)
    }

    @Async
    @EventListener
    fun handleCefrLevelReached(event: CefrLevelReachedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.REACH_CEFR_LEVEL, event.languageId, null, event.cefrLevelNumeric)
    }

    @Async
    @EventListener
    fun handleLanguageStarted(event: LanguageStartedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.LEARN_LANGUAGE_AMOUNT, null, null, event.totalLanguagesLearned)
    }

    @Async
    @EventListener
    fun handleStreakUpdated(event: StreakUpdatedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.STREAK_DAYS_AMOUNT, null, null, event.streakDays)
    }

    @Async
    @EventListener
    fun handleCityQuestsCompleted(event: CityQuestsCompletedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.COMPLETE_ALL_CITY_QUESTS, event.languageId, event.cityId, 1)
    }

    @Async
    @EventListener
    fun handleFeedbackSubmitted(event: FeedbackSubmittedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.SUBMIT_FEEDBACK, null, null, 1)
    }

    @Async
    @EventListener
    fun handlePremiumContentUnlocked(event: PremiumContentUnlockedEvent) {
        evaluationService.evaluateCondition(event.userId, AchievementConditionType.UNLOCK_PREMIUM_CONTENT, null, event.targetId, 1)
    }
}