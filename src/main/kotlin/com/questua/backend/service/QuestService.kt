package com.questua.backend.service

import com.questua.backend.model.Quest
import com.questua.backend.repository.QuestRepository
import com.questua.backend.repository.SceneDialogueRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class QuestService(
    private val repository: QuestRepository,
    private val sceneDialogueRepository: SceneDialogueRepository
) {

    fun findAll(pageable: Pageable): Page<Quest> =
        repository.findAll(pageable)

    fun findById(id: UUID): Quest =
        repository.findById(id).orElseThrow { EntityNotFoundException("Quest not found: $id") }

    fun findByQuestPointId(questPointId: UUID, pageable: Pageable): Page<Quest> =
        repository.findByQuestPointId(questPointId, pageable)

    fun create(quest: Quest): Quest {
        return repository.save(quest)
    }

    fun update(id: UUID, updated: Quest): Quest {
        val existing = findById(id)
        val merged = existing.copy(
            questPointId = updated.questPointId,
            firstDialogueId = updated.firstDialogueId,
            title = updated.title,
            descriptionQuest = updated.descriptionQuest,
            difficulty = updated.difficulty,
            orderIndex = updated.orderIndex,
            xpValue = updated.xpValue,
            xpPerQuestion = updated.xpPerQuestion, 
            unlockRequirement = updated.unlockRequirement,
            learningFocus = updated.learningFocus,
            isPremium = updated.isPremium,
            isAiGenerated = updated.isAiGenerated,
            isPublished = updated.isPublished
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("Quest not found: $id")
        repository.deleteById(id)
    }
}