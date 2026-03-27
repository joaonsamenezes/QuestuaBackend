package com.questua.backend.service

import com.questua.backend.filter.CharacterEntityFilter
import com.questua.backend.model.CharacterEntity
import com.questua.backend.repository.CharacterEntityRepository
import com.questua.backend.specification.CharacterEntitySpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CharacterEntityService(
    private val repository: CharacterEntityRepository,
    private val archiveStorageService: ArchiveStorageService
) {

    fun findAll(filter: CharacterEntityFilter, pageable: Pageable): Page<CharacterEntity> =
        repository.findAll(CharacterEntitySpecification.fromFilter(filter), pageable)

    fun findById(id: UUID): CharacterEntity =
        repository.findById(id).orElseThrow { EntityNotFoundException("Character not found: $id") }

    fun create(entity: CharacterEntity): CharacterEntity =
        repository.save(entity)

    fun update(id: UUID, updated: CharacterEntity): CharacterEntity {
        val existing = findById(id)

        val oldAvatar = existing.avatarUrl
        val newAvatar = updated.avatarUrl
        if (oldAvatar != newAvatar) {
            archiveStorageService.delete(oldAvatar)
        }

        val oldVoice = existing.voiceUrl
        val newVoice = updated.voiceUrl
        if (oldVoice != null && oldVoice != newVoice) {
            archiveStorageService.delete(oldVoice)
        }

        val merged = existing.copy(
            nameCharacter = updated.nameCharacter,
            persona = updated.persona,
            avatarUrl = newAvatar,
            spriteSheet = updated.spriteSheet,
            voiceUrl = newVoice,
            isAiGenerated = updated.isAiGenerated,
            createdAt = existing.createdAt
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        val existing = findById(id)

        archiveStorageService.delete(existing.avatarUrl)
        existing.voiceUrl?.let { archiveStorageService.delete(it) } 

        repository.deleteById(id)
    }
}
