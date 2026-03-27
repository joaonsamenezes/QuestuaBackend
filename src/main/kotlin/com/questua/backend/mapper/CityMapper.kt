package com.questua.backend.mapper

import com.questua.backend.dto.CityRequestDTO
import com.questua.backend.dto.CityResponseDTO
import com.questua.backend.model.City

object CityMapper {
    fun toEntity(dto: CityRequestDTO): City = City(
        cityName = dto.cityName,
        countryCode = dto.countryCode,
        descriptionCity = dto.descriptionCity,
        languageId = dto.languageId,
        boundingPolygon = dto.boundingPolygon,
        lat = dto.lat,
        lon = dto.lon,
        imageUrl = dto.imageUrl,
        iconUrl = dto.iconUrl,
        isPremium = dto.isPremium,
        unlockRequirement = dto.unlockRequirement,
        isAiGenerated = dto.isAiGenerated,
        isPublished = dto.isPublished
    )

    fun toResponse(entity: City): CityResponseDTO = CityResponseDTO(
        id = entity.id!!,
        cityName = entity.cityName,
        countryCode = entity.countryCode,
        descriptionCity = entity.descriptionCity,
        languageId = entity.languageId,
        boundingPolygon = entity.boundingPolygon,
        lat = entity.lat,
        lon = entity.lon,
        imageUrl = entity.imageUrl,
        iconUrl = entity.iconUrl,
        isPremium = entity.isPremium,
        unlockRequirement = entity.unlockRequirement,
        isAiGenerated = entity.isAiGenerated,
        isPublished = entity.isPublished,
        createdAt = entity.createdAt
    )
} 