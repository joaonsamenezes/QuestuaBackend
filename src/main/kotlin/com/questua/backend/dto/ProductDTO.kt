package com.questua.backend.dto

import com.questua.backend.model.TargetType
import jakarta.validation.constraints.*
import java.util.*

data class ProductRequestDTO(
    @field:NotBlank(message = "sku não pode ser vazio")
    @field:Size(max = 100, message = "sku deve ter no máximo 100 caracteres")
    val sku: String,

    @field:NotBlank(message = "title não pode ser vazio")
    @field:Size(max = 150, message = "title deve ter no máximo 150 caracteres")
    val title: String,

    @field:Size(max = 500, message = "descriptionProduct deve ter no máximo 500 caracteres")
    val descriptionProduct: String? = null,

    @field:Min(value = 0, message = "priceCents não pode ser negativo")
    val priceCents: Int,

    @field:Size(max = 3, message = "currency deve ter no máximo 3 caracteres")
    val currency: String? = "BRL",

    @field:NotNull(message = "targetType é obrigatório")
    val targetType: TargetType,

    @field:NotNull(message = "targetId é obrigatório")
    val targetId: UUID
)

data class ProductResponseDTO(
    val id: UUID,
    val sku: String,
    val title: String,
    val descriptionProduct: String?,
    val priceCents: Int,
    val currency: String,
    val targetType: TargetType,
    val targetId: UUID,
    val createdAt: Date
)
 