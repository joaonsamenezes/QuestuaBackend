package com.questua.backend.service

import com.questua.backend.filter.QuestFilter
import com.questua.backend.model.*
import com.questua.backend.repository.*
import com.questua.backend.specification.QuestSpecification
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import com.questua.backend.model.enums.ProgressStatus

enum class AccessStatus {
    ALLOWED,
    LOCKED_PREMIUM,
    LOCKED_LEVEL,
    LOCKED_SEQUENCE,
    LOCKED_UNKNOWN
}

data class AccessResult(
    val status: AccessStatus,
    val message: String? = null
)

@Service
class UnlockService(
    private val userLanguageRepository: UserLanguageRepository,
    private val userQuestRepository: UserQuestRepository,
    private val transactionRecordRepository: TransactionRecordRepository,
    private val productRepository: ProductRepository,
    private val questRepository: QuestRepository,
    private val questPointRepository: QuestPointRepository,
    private val cityRepository: CityRepository
) {
    private val cefrLevels = listOf("A1", "A2", "B1", "B2", "C1", "C2")

    @Transactional
    fun unlockContentAfterPayment(userId: UUID, productId: UUID) {
        val product = productRepository.findById(productId)
            .orElseThrow { RuntimeException("Product not found") }

        val userLanguage = userLanguageRepository.findAll()
            .firstOrNull { it.userId == userId && it.statusLanguage == StatusLanguage.ACTIVE }
            ?: throw RuntimeException("Active UserLanguage not found")

        val contentIdToUnlock = product.targetId.toString()
        val currentContent = userLanguage.unlockedContent ?: UnlockedContent()

        val updatedContent = when (product.targetType) {
            TargetType.CITY -> {
                if (!currentContent.cities.contains(contentIdToUnlock)) {
                    currentContent.copy(cities = currentContent.cities + contentIdToUnlock)
                } else currentContent
            }
            TargetType.QUEST_POINT -> {
                if (!currentContent.questPoints.contains(contentIdToUnlock)) {
                    currentContent.copy(questPoints = currentContent.questPoints + contentIdToUnlock)
                } else currentContent
            }
            TargetType.QUEST -> {
                if (!currentContent.quests.contains(contentIdToUnlock)) {
                    currentContent.copy(quests = currentContent.quests + contentIdToUnlock)
                } else currentContent
            }
        }

        if (updatedContent != currentContent) {
            userLanguage.unlockedContent = updatedContent
            userLanguageRepository.saveAndFlush(userLanguage) 
        }

        scanAndUnlockNewContent(userId, userLanguage.languageId)
    }

    fun checkAccess(userId: UUID, targetId: UUID, targetType: TargetType): AccessResult {
        var cachedQuest: Quest? = null

        if (targetType == TargetType.QUEST) {
            val isCompleted = userQuestRepository.findByUserIdAndQuestId(userId, targetId)
                ?.progressStatus == ProgressStatus.COMPLETED
            
            if (isCompleted) return AccessResult(AccessStatus.ALLOWED)

            cachedQuest = questRepository.findById(targetId).orElse(null) 
                ?: return AccessResult(AccessStatus.LOCKED_UNKNOWN, "Missão não encontrada")
            
            val parentAccess = checkAccess(userId, cachedQuest.questPointId, TargetType.QUEST_POINT)
            if (parentAccess.status != AccessStatus.ALLOWED) {
                return parentAccess
            }
        }

        if (targetType == TargetType.QUEST_POINT) {
            val questPoint = questPointRepository.findById(targetId).orElse(null)
                ?: return AccessResult(AccessStatus.LOCKED_UNKNOWN, "Ponto não encontrado")

            val cityAccess = checkAccess(userId, questPoint.cityId, TargetType.CITY)
            if (cityAccess.status != AccessStatus.ALLOWED) {
                return cityAccess
            }
        }

        val targetInfo = getTargetInfo(targetId, targetType, cachedQuest)
            ?: return AccessResult(AccessStatus.LOCKED_UNKNOWN, "Conteúdo não encontrado")
        
        val unlockRequirement = targetInfo.first
        val languageId = targetInfo.second

        if (unlockRequirement.premiumAccess) {
            if (!checkPremiumAccess(userId, targetId, targetType)) {
                return AccessResult(AccessStatus.LOCKED_PREMIUM, "Requer acesso Premium.")
            }
        }

        val userLanguage = userLanguageRepository.findByUserId(userId, Pageable.unpaged())
            .content.find { it.languageId == languageId }
            ?: return AccessResult(AccessStatus.LOCKED_LEVEL, "Idioma não iniciado.")

        unlockRequirement.requiredGamificationLevel?.let { required ->
            if (userLanguage.gamificationLevel < required) return AccessResult(AccessStatus.LOCKED_LEVEL, "Nível de Gamificação $required necessário.")
        }

        unlockRequirement.requiredCefrLevel?.let { required ->
            if (!isCefrLevelSufficient(userLanguage.cefrLevel, required)) return AccessResult(AccessStatus.LOCKED_LEVEL, "Nível CEFR $required necessário.")
        }

        if (unlockRequirement.requiredQuests.isNotEmpty()) {
            if (!areQuestsCompleted(userId, unlockRequirement.requiredQuests)) {
                return AccessResult(AccessStatus.LOCKED_SEQUENCE, "Complete as missões anteriores obrigatórias.")
            }
        }

        if (unlockRequirement.requiredQuestPoints.isNotEmpty()) {
            if (!areQuestPointsCompleted(userId, unlockRequirement.requiredQuestPoints)) {
                return AccessResult(AccessStatus.LOCKED_SEQUENCE, "Complete os pontos de missão anteriores.")
            }
        }

        // REMOVED THE AUTOMATIC SEQUENCE LOCKER HERE to decouple sequence unlocking
        // The game should rely purely on the requiredQuests specified explicitly in the database, 
        // not enforce sequential play by default anymore.

        return AccessResult(AccessStatus.ALLOWED)
    }

    @Transactional
    fun scanAndUnlockNewContent(userId: UUID, languageId: UUID) {
        val userLanguage = userLanguageRepository.findByUserId(userId, Pageable.unpaged())
            .content.find { it.languageId == languageId } ?: return

        val currentUnlocked = userLanguage.unlockedContent ?: UnlockedContent()
        
        val unlockedCities = currentUnlocked.cities.toMutableSet()
        val unlockedQuestPoints = currentUnlocked.questPoints.toMutableSet()
        val unlockedQuests = currentUnlocked.quests.toMutableSet()

        var hasChanges = false

        val allCities = cityRepository.findAll().filter { it.languageId == languageId }
        for (city in allCities) {
            if (!unlockedCities.contains(city.id.toString())) {
                if (checkAccess(userId, city.id!!, TargetType.CITY).status == AccessStatus.ALLOWED) {
                    unlockedCities.add(city.id.toString())
                    hasChanges = true
                }
            }
        }

        val cityIds = allCities.map { it.id }

        val allQuestPoints = questPointRepository.findAll().filter { it.cityId in cityIds }
        for (qp in allQuestPoints) {
            if (!unlockedQuestPoints.contains(qp.id.toString())) {
                if (checkAccess(userId, qp.id!!, TargetType.QUEST_POINT).status == AccessStatus.ALLOWED) {
                    unlockedQuestPoints.add(qp.id.toString())
                    hasChanges = true
                }
            }
        }

        val qpIds = allQuestPoints.map { it.id }

        val allQuests = questRepository.findAll().filter { it.questPointId in qpIds }
        for (quest in allQuests) {
            if (!unlockedQuests.contains(quest.id.toString())) {
                if (checkAccess(userId, quest.id!!, TargetType.QUEST).status == AccessStatus.ALLOWED) {
                    unlockedQuests.add(quest.id.toString())
                    hasChanges = true
                }
            }
        }

        if (hasChanges) {
            val updatedUnlocked = UnlockedContent(
                cities = unlockedCities.toList(),
                questPoints = unlockedQuestPoints.toList(),
                quests = unlockedQuests.toList()
            )
            userLanguage.unlockedContent = updatedUnlocked
            userLanguageRepository.saveAndFlush(userLanguage)
        }
    }

    fun resolveLanguageIdFromTarget(targetId: UUID, targetType: TargetType): UUID? {
        return getTargetInfo(targetId, targetType)?.second
    }

    private fun getTargetInfo(targetId: UUID, targetType: TargetType, cachedQuest: Quest? = null): Pair<UnlockRequirement, UUID>? {
        return when (targetType) {
            TargetType.CITY -> {
                cityRepository.findById(targetId).orElse(null)?.let { city ->
                    val req = city.unlockRequirement ?: UnlockRequirement()
                    val effectiveReq = if (city.isPremium) req.copy(premiumAccess = true) else req
                    effectiveReq to city.languageId
                }
            }
            TargetType.QUEST_POINT -> {
                questPointRepository.findById(targetId).orElse(null)?.let { qp ->
                    val city = cityRepository.findById(qp.cityId).orElse(null)
                    if (city != null) {
                        (qp.unlockRequirement ?: UnlockRequirement()) to city.languageId
                    } else null
                }
            }
            TargetType.QUEST -> {
                val q = cachedQuest ?: questRepository.findById(targetId).orElse(null)
                if (q != null) {
                    val qp = questPointRepository.findById(q.questPointId).orElse(null)
                    val city = if (qp != null) cityRepository.findById(qp.cityId).orElse(null) else null
                    if (city != null) {
                        (q.unlockRequirement ?: UnlockRequirement()) to city.languageId
                    } else null
                } else null
            }
        }
    }

    private fun checkPremiumAccess(userId: UUID, targetId: UUID, targetType: TargetType): Boolean {
        val products = productRepository.findAll().filter {
            it.targetId == targetId && it.targetType == targetType
        }

        if (products.isEmpty()) return false

        return products.any { product ->
            val spec = Specification<TransactionRecord> { root, _, cb ->
                cb.and(
                    cb.equal(root.get<UUID>("userId"), userId),
                    cb.equal(root.get<TransactionStatus>("statusTransaction"), TransactionStatus.SUCCEEDED),
                    cb.equal(root.get<UUID>("productId"), product.id)
                )
            }
            transactionRecordRepository.count(spec) > 0
        }
    }

    private fun isCefrLevelSufficient(userLevel: String, requiredLevel: String): Boolean {
        val userIndex = cefrLevels.indexOf(userLevel.uppercase())
        val requiredIndex = cefrLevels.indexOf(requiredLevel.uppercase())
        return userIndex != -1 && requiredIndex != -1 && userIndex >= requiredIndex
    }

    private fun areQuestsCompleted(userId: UUID, requiredQuestIds: List<UUID>): Boolean {
        if (requiredQuestIds.isEmpty()) return true
        
        return requiredQuestIds.all { questId ->
            userQuestRepository.findByUserIdAndQuestId(userId, questId)
                ?.progressStatus == ProgressStatus.COMPLETED
        }
    }

    private fun areQuestPointsCompleted(userId: UUID, requiredQuestPointIds: List<UUID>): Boolean {
        return requiredQuestPointIds.all { qpId ->
            val filter = QuestFilter(questPointId = qpId)
            val spec = QuestSpecification.fromFilter(filter)
            val questsInPoint = questRepository.findAll(spec)
 
            if (questsInPoint.isEmpty()) {
                true
            } else {
                val questIds = questsInPoint.mapNotNull { it.id }
                areQuestsCompleted(userId, questIds)
            }
        }
    }
}