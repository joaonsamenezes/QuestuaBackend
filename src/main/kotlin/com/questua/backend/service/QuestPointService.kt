package com.questua.backend.service

import com.questua.backend.dto.QuestPointResponseDTO
import com.questua.backend.filter.QuestPointFilter
import com.questua.backend.mapper.QuestPointMapper
import com.questua.backend.model.QuestPoint
import com.questua.backend.model.TargetType
import com.questua.backend.repository.QuestPointRepository
import com.questua.backend.specification.QuestPointSpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class QuestPointService(
    private val repository: QuestPointRepository,
    private val unlockService: UnlockService
) {

    fun findAll(filter: QuestPointFilter, pageable: Pageable): Page<QuestPoint> =
        repository.findAll(QuestPointSpecification.fromFilter(filter), pageable)

    fun findForUser(userId: UUID, filter: QuestPointFilter, pageable: Pageable): Page<QuestPointResponseDTO> {
        val publicFilter = filter.copy(isPublished = true)

        return findAll(publicFilter, pageable).map { qp ->
            val access = unlockService.checkAccess(userId, qp.id!!, TargetType.QUEST_POINT)
            val isLocked = access.status != AccessStatus.ALLOWED

            val response = QuestPointMapper.toResponse(qp).copy(
                isLocked = isLocked,
                lockMessage = access.message
            )

            if (isLocked) {
                response.copy(lat = null, lon = null)
            } else {
                response
            }
        }
    }

    fun findById(id: UUID): QuestPoint =
        repository.findById(id).orElseThrow { EntityNotFoundException("QuestPoint not found: $id") }

    fun create(entity: QuestPoint): QuestPoint =
        repository.save(entity)

    fun update(id: UUID, updated: QuestPoint): QuestPoint {
        val existing = findById(id)
        val merged = existing.copy(
            cityId = updated.cityId,
            title = updated.title,
            descriptionQpoint = updated.descriptionQpoint,
            difficulty = updated.difficulty,
            lat = updated.lat,
            lon = updated.lon,
            imageUrl = updated.imageUrl ?: existing.imageUrl,
            iconUrl = updated.iconUrl ?: existing.iconUrl,
            unlockRequirement = updated.unlockRequirement,
            isPremium = updated.isPremium,
            isAiGenerated = updated.isAiGenerated,
            isPublished = updated.isPublished
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("QuestPoint not found: $id")
        repository.deleteById(id)
    }
}