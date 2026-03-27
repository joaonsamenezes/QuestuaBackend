package com.questua.backend.event

import java.util.UUID

data class QuestCompletedEvent(
    val userId: UUID, 
    val questId: UUID, 
    val languageId: UUID?, 
    val durationSeconds: Long, 
    val isPerfect: Boolean
)

data class CityUnlockedEvent(
    val userId: UUID, 
    val cityId: UUID, 
    val languageId: UUID?
)

data class LevelUpEvent(
    val userId: UUID, 
    val newLevel: Int
)

data class XpEarnedEvent(
    val userId: UUID, 
    val totalXp: Int
)

data class QuestPointUnlockedEvent(
    val userId: UUID,
    val questPointId: UUID,
    val languageId: UUID?
)

data class CefrLevelReachedEvent(
    val userId: UUID,
    val languageId: UUID?,
    val cefrLevelNumeric: Int
)

data class LanguageStartedEvent(
    val userId: UUID,
    val totalLanguagesLearned: Int
)

data class StreakUpdatedEvent(
    val userId: UUID,
    val streakDays: Int
)

data class CityQuestsCompletedEvent(
    val userId: UUID,
    val cityId: UUID,
    val languageId: UUID?
)

data class FeedbackSubmittedEvent(
    val userId: UUID
)

data class PremiumContentUnlockedEvent(
    val userId: UUID,
    val targetId: UUID
)

data class PurchaseEvent(val userId: UUID, val productId: String?, val amountCents: Int?)