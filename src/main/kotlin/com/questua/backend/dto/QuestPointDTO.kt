package com.questua.backend.dto

import com.questua.backend.model.UnlockRequirement
import jakarta.validation.constraints.*
import java.util.*

data class QuestPointRequestDTO(
    @field:NotNull(message = "cityId é obrigatório")
    val cityId: UUID,

    @field:NotBlank(message = "title não pode ser vazio")
    @field:Size(max = 150, message = "title deve ter no máximo 150 caracteres")
    val title: String,

    @field:NotBlank(message = "descriptionQpoint não pode ser vazio")
    val descriptionQpoint: String,

    @field:Min(value = 1, message = "difficulty deve ser >= 1")
    val difficulty: Short = 1,

    @field:NotNull(message = "lat é obrigatório")
    val lat: Double,

    @field:NotNull(message = "lon é obrigatório")
    val lon: Double,

    @field:Size(max = 255, message = "imageUrl deve ter no máximo 255 caracteres")
    val imageUrl: String? = null,

    @field:Size(max = 255, message = "iconUrl deve ter no máximo 255 caracteres")
    val iconUrl: String? = null,

    val unlockRequirement: UnlockRequirement? = null,
    val isPremium: Boolean = false,
    val isAiGenerated: Boolean = false,
    val isPublished: Boolean = false
)
data class QuestPointResponseDTO(
    val id: UUID,
    val cityId: UUID,
    val title: String,
    val descriptionQpoint: String,
    val difficulty: Short,
    val lat: Double?,
    val lon: Double?,
    val imageUrl: String?,
    val iconUrl: String?,
    val unlockRequirement: UnlockRequirement?,
    val isPremium: Boolean,
    val isAiGenerated: Boolean,
    val isPublished: Boolean,
    val createdAt: Date,
    val isLocked: Boolean = false,
    val lockMessage: String? = null
)