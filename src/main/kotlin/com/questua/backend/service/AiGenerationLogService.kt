package com.questua.backend.service

import com.questua.backend.filter.AiGenerationLogFilter
import com.questua.backend.model.AiGenerationLog
import com.questua.backend.repository.AiGenerationLogRepository
import com.questua.backend.specification.AiGenerationLogSpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AiGenerationLogService(private val repository: AiGenerationLogRepository) {

    fun findAll(filter: AiGenerationLogFilter, pageable: Pageable): Page<AiGenerationLog> =
        repository.findAll(AiGenerationLogSpecification.fromFilter(filter), pageable)

    fun findById(id: UUID): AiGenerationLog =
        repository.findById(id).orElseThrow { EntityNotFoundException("AiGenerationLog not found: $id") }

    fun create(entity: AiGenerationLog): AiGenerationLog =
        repository.save(entity)

    fun update(id: UUID, updated: AiGenerationLog): AiGenerationLog {
        val existing = findById(id)
        val merged = existing.copy(
            userId = updated.userId,
            targetType = updated.targetType,
            targetId = updated.targetId,
            prompt = updated.prompt,
            modelName = updated.modelName,
            responseText = updated.responseText,
            responseMeta = updated.responseMeta,
            statusGeneration = updated.statusGeneration
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("AiGenerationLog not found: $id")
        repository.deleteById(id)
    } 
}
 