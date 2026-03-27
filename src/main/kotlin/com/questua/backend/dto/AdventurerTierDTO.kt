package com.questua.backend.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.Date
import java.util.UUID

data class AdventurerTierRequestDTO(
    @field:NotBlank
    val keyName: String,
    @field:NotBlank
    val nameDisplay: String,
    val iconUrl: String?,
    val colorHex: String?,
    @field:NotNull
    val orderIndex: Int,
    @field:NotNull
    @field:Min(1)
    val levelRequired: Int
)

data class AdventurerTierResponseDTO(
    val id: UUID,
    val keyName: String,
    val nameDisplay: String,
    val iconUrl: String?,
    val colorHex: String?,
    val orderIndex: Int,
    val levelRequired: Int,
    val createdAt: Date
)