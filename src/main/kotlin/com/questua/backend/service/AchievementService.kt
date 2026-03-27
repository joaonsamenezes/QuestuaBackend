package com.questua.backend.service

import com.questua.backend.filter.AchievementFilter
import com.questua.backend.model.Achievement
import com.questua.backend.repository.AchievementRepository
import com.questua.backend.specification.AchievementSpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AchievementService(
    private val repository: AchievementRepository,
    private val archiveStorageService: ArchiveStorageService 
) {

    fun findAll(filter: AchievementFilter, pageable: Pageable): Page<Achievement> =
        repository.findAll(AchievementSpecification.fromFilter(filter), pageable)

    fun findById(id: UUID): Achievement =
        repository.findById(id).orElseThrow { EntityNotFoundException("Achievement not found: $id") }

    fun create(entity: Achievement): Achievement =
        repository.save(entity)

    fun update(id: UUID, updated: Achievement): Achievement {
        val existing = findById(id)

        val oldIcon = existing.iconUrl
        val newIcon = updated.iconUrl
        if (oldIcon != null && oldIcon != newIcon) {
            archiveStorageService.delete(oldIcon)
        }

        val merged = existing.copy(
            keyName = updated.keyName,
            nameAchievement = updated.nameAchievement,
            descriptionAchievement = updated.descriptionAchievement,
            iconUrl = newIcon,
            rarity = updated.rarity,
            xpReward = updated.xpReward,
            metadata = updated.metadata
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        val existing = findById(id)
        existing.iconUrl?.let { archiveStorageService.delete(it) }
        repository.deleteById(id)
    }
}
 