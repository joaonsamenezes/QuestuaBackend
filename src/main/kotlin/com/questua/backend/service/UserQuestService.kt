package com.questua.backend.service

import com.questua.backend.dto.SubmitResponseRequestDTO
import com.questua.backend.dto.SubmitResponseResultDTO
import com.questua.backend.event.*
import com.questua.backend.mapper.UserQuestMapper
import com.questua.backend.model.*
import com.questua.backend.model.enums.*
import com.questua.backend.repository.*
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date
import java.util.UUID

@Service
class UserQuestService(
    private val repository: UserQuestRepository,
    private val sceneDialogueRepository: SceneDialogueRepository,
    private val userLanguageService: UserLanguageService,
    private val unlockService: UnlockService,
    private val questRepository: QuestRepository,
    private val questPointRepository: QuestPointRepository,
    private val cityRepository: CityRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun findAll(pageable: Pageable): Page<UserQuest> =
        repository.findAll(pageable)

    fun findById(id: UUID): UserQuest =
        repository.findById(id).orElseThrow { EntityNotFoundException("UserQuest not found: $id") }

    fun findByUser(userId: UUID, pageable: Pageable): Page<UserQuest> =
        repository.findByUserId(userId, pageable)

    fun findByQuest(questId: UUID, pageable: Pageable): Page<UserQuest> =
        repository.findByQuestId(questId, pageable)

    fun findByUserAndQuest(userId: UUID, questId: UUID): UserQuest? =
        repository.findByUserIdAndQuestId(userId, questId)

    fun create(userQuest: UserQuest): UserQuest {
        userQuest.xpEarned = 0
        userQuest.score = 0
        userQuest.percentComplete = 0.0
        
        if (userQuest.startedAt == null) {
            userQuest.startedAt = Date()
        }
        return repository.save(userQuest)
    }

    fun update(id: UUID, updated: UserQuest): UserQuest {
        val existing = findById(id)
        val merged = existing.copy(
            userId = updated.userId,
            questId = updated.questId,
            progressStatus = updated.progressStatus,
            xpEarned = updated.xpEarned,
            score = updated.score,
            percentComplete = updated.percentComplete,
            lastDialogueId = updated.lastDialogueId,
            startedAt = updated.startedAt ?: existing.startedAt,
            lastActivityAt = updated.lastActivityAt,
            completedAt = updated.completedAt,
            responses = updated.responses,
            overallAssessment = updated.overallAssessment
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("UserQuest not found: $id")
        repository.deleteById(id)
    }

    @Transactional
    fun submitResponse(userQuestId: UUID, request: SubmitResponseRequestDTO): SubmitResponseResultDTO {
        val userQuest = findById(userQuestId)

        if (userQuest.progressStatus == ProgressStatus.COMPLETED) {
             return SubmitResponseResultDTO(
                 correct = true,
                 feedback = "Quest já completada.",
                 nextDialogueId = null,
                 xpEarned = 0,
                 isQuestCompleted = true,
                 userQuest = UserQuestMapper.toResponse(userQuest)
             )
        }

        val quest = questRepository.findById(userQuest.questId)
            .orElseThrow { EntityNotFoundException("Quest not found: ${userQuest.questId}") }

        val currentDialogue = sceneDialogueRepository.findById(request.dialogueId)
            .orElseThrow { EntityNotFoundException("SceneDialogue not found: ${request.dialogueId}") }

        val isCorrect = validateResponse(currentDialogue, request.answer)
        val expectsResponse = currentDialogue.expectsUserResponse

        val alreadyAnsweredCorrectly = userQuest.responses?.any { 
            it.questionId == currentDialogue.id && it.correct == true 
        } == true

        val isRealQuestion = !currentDialogue.expectedResponse.isNullOrBlank()

        val xpPerHit = quest.xpPerQuestion
        var xpGainedCurrentTurn = 0

        if (isCorrect && expectsResponse && isRealQuestion && !alreadyAnsweredCorrectly) {
            userQuest.score = (userQuest.score ?: 0) + 1
            xpGainedCurrentTurn = xpPerHit
            userQuest.xpEarned += xpPerHit
        }

        val recordedCorrectness = if (expectsResponse) isCorrect else null

        val recordedFeedback = if (expectsResponse) {
            if (isCorrect) "Correto!"
            else "Incorreto. A resposta correta era: ${currentDialogue.expectedResponse ?: "N/A"}"
        } else {
            null
        }

        val newResponse = Response(
            questionId = currentDialogue.id!!,
            answer = request.answer,
            correct = recordedCorrectness,
            feedback = recordedFeedback
        )

        val updatedResponses = userQuest.responses?.toMutableList() ?: mutableListOf()
        updatedResponses.add(newResponse)
        userQuest.responses = updatedResponses
        userQuest.lastActivityAt = Date()

        val nextDialogueId = determineNextDialogue(currentDialogue, request.answer)

        if (nextDialogueId != null) {
            userQuest.lastDialogueId = nextDialogueId
        }

        val totalSteps = calculateTotalSteps(quest)
        val currentStep = updatedResponses.size.toDouble()

        val calculatedPercent = if (totalSteps > 0) {
            (currentStep / totalSteps.toDouble()) * 100.0
        } else {
            100.0
        }

        userQuest.percentComplete = calculatedPercent.coerceAtMost(100.0)

        if (nextDialogueId == null) {
            userQuest.percentComplete = 100.0
            completeQuest(userQuest, quest)
        }

        val savedUserQuest = repository.save(userQuest)

        return SubmitResponseResultDTO(
            correct = isCorrect,
            feedback = newResponse.feedback,
            nextDialogueId = nextDialogueId,
            xpEarned = xpGainedCurrentTurn,
            isQuestCompleted = nextDialogueId == null,
            userQuest = UserQuestMapper.toResponse(savedUserQuest)
        )
    }

    private fun validateResponse(dialogue: SceneDialogue, answer: String): Boolean {
        if (!dialogue.expectsUserResponse) return true
        
        val expected = dialogue.expectedResponse?.trim()
        if (expected.isNullOrEmpty()) return true

        return expected.equals(answer.trim(), ignoreCase = true)
    }

    private fun determineNextDialogue(currentDialogue: SceneDialogue, answer: String): UUID? {
        if (currentDialogue.inputMode == InputMode.CHOICE && currentDialogue.choices != null) {
            val selectedChoice = currentDialogue.choices!!.find {
                it.text.equals(answer, ignoreCase = true)
            }
            return selectedChoice?.nextDialogueId ?: currentDialogue.nextDialogueId
        }
        return currentDialogue.nextDialogueId
    }

    private fun calculateTotalSteps(quest: Quest): Int {
        var currentId = quest.firstDialogueId ?: return 0
        var count = 0
        val maxSteps = 100

        while (count < maxSteps) {
            count++
            val dialogue = sceneDialogueRepository.findById(currentId).orElse(null) ?: break
            if (dialogue.nextDialogueId == null) break
            currentId = dialogue.nextDialogueId!!
        }

        return count
    }

    private fun completeQuest(userQuest: UserQuest, quest: Quest) {
        userQuest.progressStatus = ProgressStatus.COMPLETED
        userQuest.completedAt = Date()

        val questCompletionXp = quest.xpValue 
        userQuest.xpEarned += questCompletionXp

        generateOverallAssessment(userQuest)
        
        val questPoint = questPointRepository.findById(quest.questPointId)
            .orElseThrow { EntityNotFoundException("QuestPoint not found: ${quest.questPointId}") }

        val city = cityRepository.findById(questPoint.cityId)
            .orElseThrow { EntityNotFoundException("City not found: ${questPoint.cityId}") }

        userLanguageService.addExperienceByUserIdAndLanguageId(userQuest.userId, city.languageId, userQuest.xpEarned)
        userLanguageService.incrementQuestCountByUserIdAndLanguageId(userQuest.userId, city.languageId)

        unlockService.scanAndUnlockNewContent(userQuest.userId, city.languageId)

        val totalQuestions = userQuest.responses?.count { it.correct != null } ?: 0
        val correctCount = userQuest.score ?: 0
        val isPerfect = totalQuestions > 0 && correctCount == totalQuestions

        val startTime = userQuest.startedAt?.time ?: userQuest.lastActivityAt?.time ?: userQuest.completedAt?.time ?: Date().time
        val endTime = userQuest.completedAt?.time ?: Date().time
        val durationSeconds = (endTime - startTime) / 1000

        eventPublisher.publishEvent(
            QuestCompletedEvent(
                userId = userQuest.userId,
                questId = quest.id!!,
                languageId = city.languageId,
                durationSeconds = durationSeconds,
                isPerfect = isPerfect
            )
        )

        checkAndPublishCityCompletion(userQuest.userId, city.id!!, city.languageId)
    }

    private fun generateOverallAssessment(userQuest: UserQuest) {
        val responses = userQuest.responses ?: emptyList()
        val totalQuestions = responses.count { it.correct != null }

        if (totalQuestions == 0) return

        val correctCount = userQuest.score ?: 0
        val accuracy = (correctCount.toDouble() / totalQuestions.toDouble() * 100).toInt()

        val feedbackMessage = when {
            accuracy == 100 -> "Perfeito! Domínio total."
            accuracy >= 70 -> "Muito bom! Continue praticando."
            else -> "Revise os conceitos e tente novamente."
        }

        userQuest.overallAssessment = listOf(
            SkillAssessment(
                skill = "General Accuracy",
                score = accuracy,
                feedback = feedbackMessage
            )
        )
    }

    private fun checkAndPublishCityCompletion(userId: UUID, cityId: UUID, languageId: UUID) {
        val userQuests = repository.findByUserId(userId, Pageable.unpaged()).content
        val completedQuestIds = userQuests.filter { it.progressStatus == ProgressStatus.COMPLETED }.map { it.questId }.toSet()
        
        val allQuestPoints = questPointRepository.findAll().filter { it.cityId == cityId }
        val allCityQuestIds = questRepository.findAll()
            .filter { quest -> allQuestPoints.any { it.id == quest.questPointId } }
            .map { it.id!! }
            .toSet()

        if (allCityQuestIds.isNotEmpty() && completedQuestIds.containsAll(allCityQuestIds)) {
            eventPublisher.publishEvent(
                CityQuestsCompletedEvent(userId, cityId, languageId)
            )
        }
    }
}