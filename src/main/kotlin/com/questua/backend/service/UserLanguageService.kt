package com.questua.backend.service

import com.questua.backend.event.*
import com.questua.backend.model.UserLanguage
import com.questua.backend.repository.AdventurerTierRepository
import com.questua.backend.repository.UserLanguageRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.UUID

@Service
class UserLanguageService(
    private val repository: UserLanguageRepository,
    private val adventurerTierRepository: AdventurerTierRepository,
    @param:Lazy private val unlockService: UnlockService,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun findAll(pageable: Pageable): Page<UserLanguage> =
        repository.findAll(pageable)

    fun findById(id: UUID): UserLanguage =
        repository.findById(id).orElseThrow { EntityNotFoundException("UserLanguage not found: $id") }

    fun findByUserId(userId: UUID, pageable: Pageable): Page<UserLanguage> {
        val userLanguagesPage = repository.findByUserId(userId, pageable)
            
        var hasChanges = false
        val now = Date()
        val zoneId = ZoneId.of("America/Sao_Paulo")
        val today = now.toInstant().atZone(zoneId).toLocalDate()

        userLanguagesPage.content.forEach { userLanguage ->
            userLanguage.lastActiveAt?.let { lastActive ->
                val lastActiveDate = lastActive.toInstant().atZone(zoneId).toLocalDate()
                val daysBetween = ChronoUnit.DAYS.between(lastActiveDate, today)
                
                if (daysBetween > 1L && userLanguage.streakDays > 0) {
                    userLanguage.streakDays = 0
                    hasChanges = true
                }
            }

            try {
                unlockService.scanAndUnlockNewContent(userId, userLanguage.languageId)
                hasChanges = true
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        }
        
        if (hasChanges) {
            repository.saveAll(userLanguagesPage.content)
        }
        
        return if (hasChanges) repository.findByUserId(userId, pageable) else userLanguagesPage
    }

    fun findByLanguageId(languageId: UUID, pageable: Pageable): Page<UserLanguage> =
        repository.findByLanguageId(languageId, pageable)

    fun create(userLanguage: UserLanguage): UserLanguage {
        checkAdventurerTierProgression(userLanguage)
        val saved = repository.save(userLanguage)
        
        val totalLanguages = repository.findByUserId(userLanguage.userId, Pageable.unpaged()).totalElements.toInt()
        eventPublisher.publishEvent(LanguageStartedEvent(userLanguage.userId, totalLanguages))
        
        try {
            unlockService.scanAndUnlockNewContent(saved.userId, saved.languageId)
        } catch (e: Exception) {
            System.err.println(e.message)
        }
        
        return findById(saved.id!!)
    }

    fun update(id: UUID, updated: UserLanguage): UserLanguage {
        val existing = findById(id)
        val merged = existing.copy(
            userId = updated.userId,
            languageId = updated.languageId,
            statusLanguage = updated.statusLanguage,
            cefrLevel = updated.cefrLevel,
            questsTowardsNextLevel = updated.questsTowardsNextLevel,
            gamificationLevel = updated.gamificationLevel,
            xpTotal = updated.xpTotal,
            streakDays = updated.streakDays,
            unlockedContent = updated.unlockedContent,
            startedAt = updated.startedAt,
            lastActiveAt = updated.lastActiveAt
        )
        checkAdventurerTierProgression(merged)
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("UserLanguage not found: $id")
        repository.deleteById(id)
    }

    fun addExperience(userLanguageId: UUID, xpAmount: Int): UserLanguage {
        val userLanguage = findById(userLanguageId)
        
        val bonusXp = processStreakLogic(userLanguage)
        
        eventPublisher.publishEvent(
            StreakUpdatedEvent(
                userId = userLanguage.userId,
                streakDays = userLanguage.streakDays
            )
        )

        userLanguage.xpTotal += xpAmount + bonusXp
        
        eventPublisher.publishEvent(
            XpEarnedEvent(
                userId = userLanguage.userId,
                totalXp = userLanguage.xpTotal
            )
        )
        
        var levelChanged = false

        val newLevel = (userLanguage.xpTotal / 1000) + 1
        if (newLevel > userLanguage.gamificationLevel) {
            userLanguage.gamificationLevel = newLevel
            levelChanged = true
            
            eventPublisher.publishEvent(
                LevelUpEvent(
                    userId = userLanguage.userId,
                    newLevel = newLevel
                )
            )
        }

        checkAdventurerTierProgression(userLanguage)
        repository.save(userLanguage)

        if (levelChanged) {
            try {
                unlockService.scanAndUnlockNewContent(userLanguage.userId, userLanguage.languageId)
            } catch (e: Exception) {
                 System.err.println(e.message)
            }
        }

        return findById(userLanguageId) 
    }

    fun addExperienceByUserIdAndLanguageId(userId: UUID, languageId: UUID, xpAmount: Int): UserLanguage {
        val userLanguage = repository.findByUserId(userId, Pageable.unpaged())
            .content
            .find { it.languageId == languageId }
            ?: throw EntityNotFoundException("UserLanguage not found for user $userId and language $languageId")
        
        return addExperience(userLanguage.id!!, xpAmount)
    }

    @Transactional
    fun incrementQuestCountByUserIdAndLanguageId(userId: UUID, languageId: UUID) {
        val userLanguage = repository.findByUserId(userId, Pageable.unpaged())
            .content
            .find { it.languageId == languageId }
            ?: throw EntityNotFoundException("UserLanguage not found for user $userId and language $languageId")

        if (userLanguage.cefrLevel.uppercase() == "C2") return

        userLanguage.questsTowardsNextLevel += 1

        val threshold = when (userLanguage.cefrLevel.uppercase()) {
            "A1" -> 20
            "A2" -> 30
            "B1" -> 50
            "B2" -> 50
            "C1" -> 50
            else -> Int.MAX_VALUE
        }

        if (userLanguage.questsTowardsNextLevel >= threshold) {
            userLanguage.cefrLevel = when (userLanguage.cefrLevel.uppercase()) {
                "A1" -> "A2"
                "A2" -> "B1"
                "B1" -> "B2"
                "B2" -> "C1"
                "C1" -> "C2"
                else -> userLanguage.cefrLevel
            }
            userLanguage.questsTowardsNextLevel = 0

            val cefrNumeric = when(userLanguage.cefrLevel) {
                "A1" -> 1
                "A2" -> 2
                "B1" -> 3
                "B2" -> 4
                "C1" -> 5
                "C2" -> 6
                else -> 1
            }

            eventPublisher.publishEvent(
                CefrLevelReachedEvent(
                    userId = userLanguage.userId,
                    languageId = userLanguage.languageId,
                    cefrLevelNumeric = cefrNumeric
                )
            )
        }
        repository.save(userLanguage)
    }

    fun save(userLanguage: UserLanguage): UserLanguage {
        checkAdventurerTierProgression(userLanguage)
        return repository.save(userLanguage)
    }

    fun getLeaderboard(adventurerTierId: UUID, cefrLevel: String, pageable: Pageable): Page<UserLanguage> {
        return repository.findByAdventurerTierIdAndCefrLevelOrderByXpTotalDesc(adventurerTierId, cefrLevel, pageable)
    }

    private fun checkAdventurerTierProgression(userLanguage: UserLanguage) {
        val eligibleTier = adventurerTierRepository.findFirstByLevelRequiredLessThanEqualOrderByOrderIndexDesc(userLanguage.gamificationLevel)
        if (eligibleTier != null && userLanguage.adventurerTier?.id != eligibleTier.id) {
            userLanguage.adventurerTier = eligibleTier
        }
    }

    private fun processStreakLogic(userLanguage: UserLanguage): Int {
        val now = Date()
        val zoneId = ZoneId.of("America/Sao_Paulo")
        
        val today = now.toInstant().atZone(zoneId).toLocalDate()
        val lastActiveDate = userLanguage.lastActiveAt?.toInstant()?.atZone(zoneId)?.toLocalDate()
        
        var bonusXp = 0
        
        if (lastActiveDate == null) {
            userLanguage.streakDays = 1
        } else if (!lastActiveDate.isEqual(today)) {
            val daysBetween = ChronoUnit.DAYS.between(lastActiveDate, today)
            
            if (daysBetween == 1L) {
                userLanguage.streakDays += 1
                bonusXp = 50 
            } else {
                userLanguage.streakDays = 1
            }
        }
        userLanguage.lastActiveAt = now
        return bonusXp
    }
}