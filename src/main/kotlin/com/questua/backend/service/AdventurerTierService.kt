package com.questua.backend.service

import com.questua.backend.model.AdventurerTier
import com.questua.backend.repository.AdventurerTierRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AdventurerTierService(
    private val repository: AdventurerTierRepository
) {
    fun findAll(pageable: Pageable): Page<AdventurerTier> =
        repository.findAll(pageable)

    fun findById(id: UUID): AdventurerTier =
        repository.findById(id).orElseThrow { EntityNotFoundException() }

    fun create(entity: AdventurerTier): AdventurerTier =
        repository.save(entity)

    fun update(id: UUID, updated: AdventurerTier): AdventurerTier {
        val existing = findById(id)
        val merged = existing.copy(
            keyName = updated.keyName,
            nameDisplay = updated.nameDisplay,
            iconUrl = updated.iconUrl,
            colorHex = updated.colorHex,
            orderIndex = updated.orderIndex,
            levelRequired = updated.levelRequired
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException()
        repository.deleteById(id)
    }
}