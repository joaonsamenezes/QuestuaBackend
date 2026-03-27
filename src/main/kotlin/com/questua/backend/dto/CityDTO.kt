package com.questua.backend.dto

import com.questua.backend.model.BoundingPolygon
import com.questua.backend.model.UnlockRequirement
import jakarta.validation.constraints.*
import java.util.*

data class CityRequestDTO(
    @field:NotBlank(message = "cityName não pode ser vazio")
    @field:Size(max = 100, message = "cityName deve ter no máximo 100 caracteres")
    val cityName: String,

    @field:NotBlank(message = "countryCode não pode ser vazio")
    @field:Size(max = 10, message = "countryCode deve ter no máximo 10 caracteres")
    val countryCode: String,

    @field:NotBlank(message = "descriptionCity não pode ser vazio")
    val descriptionCity: String,

    @field:NotNull(message = "languageId é obrigatório")
    val languageId: UUID,

    val boundingPolygon: BoundingPolygon? = null,

    @field:NotNull(message = "lat é obrigatório")
    val lat: Double,

    @field:NotNull(message = "lon é obrigatório")
    val lon: Double,

    @field:Size(max = 255, message = "imageUrl deve ter no máximo 255 caracteres")
    val imageUrl: String? = null,

    @field:Size(max = 255, message = "iconUrl deve ter no máximo 255 caracteres")
    val iconUrl: String? = null,

    val isPremium: Boolean = false,
    val unlockRequirement: UnlockRequirement? = null,
    val isAiGenerated: Boolean = false, 
    val isPublished: Boolean = false
)

data class CityResponseDTO(
    val id: UUID,
    val cityName: String,
    val countryCode: String,
    val descriptionCity: String,
    val languageId: UUID,
    val boundingPolygon: BoundingPolygon?,
    val lat: Double,
    val lon: Double,
    val imageUrl: String?,
    val iconUrl: String?,
    val isPremium: Boolean,
    val unlockRequirement: UnlockRequirement?,
    val isAiGenerated: Boolean,
    val isPublished: Boolean,
    val createdAt: Date,
    val isLocked: Boolean = false,
    val lockMessage: String? = null
)