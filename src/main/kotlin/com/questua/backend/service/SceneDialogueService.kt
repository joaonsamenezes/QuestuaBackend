package com.questua.backend.service

import com.questua.backend.filter.SceneDialogueFilter
import com.questua.backend.model.SceneDialogue
import com.questua.backend.repository.SceneDialogueRepository
import com.questua.backend.specification.SceneDialogueSpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SceneDialogueService(
    private val repository: SceneDialogueRepository,
    private val archiveStorageService: ArchiveStorageService
) {

    fun findAll(filter: SceneDialogueFilter, pageable: Pageable): Page<SceneDialogue> =
        repository.findAll(SceneDialogueSpecification.fromFilter(filter), pageable)

    fun findById(id: UUID): SceneDialogue =
        repository.findById(id).orElseThrow { EntityNotFoundException("SceneDialogue not found: $id") }

    fun create(dialogue: SceneDialogue): SceneDialogue =
        repository.save(dialogue)

    fun update(id: UUID, updated: SceneDialogue): SceneDialogue {
        val existing = findById(id)

        val oldBg = existing.backgroundUrl
        val newBg = updated.backgroundUrl
        if (oldBg != newBg) archiveStorageService.delete(oldBg)

        val oldMusic = existing.bgMusicUrl
        val newMusic = updated.bgMusicUrl
        if (oldMusic != null && oldMusic != newMusic) archiveStorageService.delete(oldMusic)

        val oldAudio = existing.audioUrl
        val newAudio = updated.audioUrl
        if (oldAudio != null && oldAudio != newAudio) archiveStorageService.delete(oldAudio)

        val merged = existing.copy(
            descriptionDialogue = updated.descriptionDialogue,
            backgroundUrl = newBg,
            bgMusicUrl = newMusic,
            characterStates = updated.characterStates,
            sceneEffects = updated.sceneEffects,
            speakerCharacterId = updated.speakerCharacterId,
            textContent = updated.textContent,
            audioUrl = newAudio,
            expectsUserResponse = updated.expectsUserResponse,
            inputMode = updated.inputMode,
            expectedResponse = updated.expectedResponse,
            choices = updated.choices,
            nextDialogueId = updated.nextDialogueId,
            isAiGenerated = updated.isAiGenerated
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        val existing = findById(id)

        archiveStorageService.delete(existing.backgroundUrl)
        existing.bgMusicUrl?.let { archiveStorageService.delete(it) }
        existing.audioUrl?.let { archiveStorageService.delete(it) }

        repository.deleteById(id)
    } 
}
  