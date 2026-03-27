package com.questua.backend.service

import com.questua.backend.model.Language
import com.questua.backend.repository.LanguageRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LanguageService(
    private val repository: LanguageRepository,
    private val archiveStorageService: ArchiveStorageService
) {

    fun search(filter: String?, pageable: Pageable): Page<Language> =
        if (filter.isNullOrBlank())
            repository.findAll(pageable)
        else
            repository.searchByCodeOrName(filter.trim(), pageable)

    fun findById(id: UUID): Language =
        repository.findById(id).orElseThrow { EntityNotFoundException("Language not found: $id") }

    fun findByCode(code: String): Language? =
        repository.findByCodeLanguage(code)

    fun create(language: Language): Language =
        repository.save(language)

    fun update(id: UUID, updated: Language): Language {
        val existing = findById(id)

        val oldIcon = existing.iconUrl
        val newIcon = updated.iconUrl
        if (oldIcon != null && oldIcon != newIcon) archiveStorageService.delete(oldIcon)

        val merged = existing.copy(
            codeLanguage = updated.codeLanguage,
            nameLanguage = updated.nameLanguage,
            iconUrl = newIcon
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        val existing = findById(id)
        existing.iconUrl?.let { archiveStorageService.delete(it) }
        repository.deleteById(id)
    } 
}
