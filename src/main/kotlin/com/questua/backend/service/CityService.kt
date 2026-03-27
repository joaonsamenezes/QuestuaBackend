package com.questua.backend.service

import com.questua.backend.dto.CityResponseDTO
import com.questua.backend.filter.CityFilter
import com.questua.backend.mapper.CityMapper
import com.questua.backend.model.City
import com.questua.backend.model.TargetType
import com.questua.backend.repository.CityRepository
import com.questua.backend.specification.CitySpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CityService(
    private val repository: CityRepository,
    private val unlockService: UnlockService
) {

    fun findAll(filter: CityFilter, pageable: Pageable): Page<City> =
        repository.findAll(CitySpecification.fromFilter(filter), pageable)

    fun findForUser(userId: UUID, filter: CityFilter, pageable: Pageable): Page<CityResponseDTO> {
        val publicFilter = filter.copy(isPublished = true)
        
        return findAll(publicFilter, pageable).map { city ->
            val access = unlockService.checkAccess(userId, city.id!!, TargetType.CITY)
            val isLocked = access.status != AccessStatus.ALLOWED
            
            CityMapper.toResponse(city).copy(
                isLocked = isLocked,
                lockMessage = access.message
            )
        }
    }

    fun findById(id: UUID): City =
        repository.findById(id).orElseThrow { EntityNotFoundException("City not found: $id") }

    fun create(entity: City): City =
        repository.save(entity)

    fun update(id: UUID, updated: City): City {
        val existing = findById(id)
        val merged = existing.copy(
            cityName = updated.cityName,
            countryCode = updated.countryCode,
            descriptionCity = updated.descriptionCity,
            languageId = updated.languageId,
            boundingPolygon = updated.boundingPolygon,
            lat = updated.lat,
            lon = updated.lon,
            imageUrl = updated.imageUrl ?: existing.imageUrl,
            iconUrl = updated.iconUrl ?: existing.iconUrl,
            isPremium = updated.isPremium,
            unlockRequirement = updated.unlockRequirement,
            isAiGenerated = updated.isAiGenerated,
            isPublished = updated.isPublished
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("City not found: $id")
        repository.deleteById(id)
    }
}