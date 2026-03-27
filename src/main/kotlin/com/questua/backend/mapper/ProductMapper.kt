package com.questua.backend.mapper

import com.questua.backend.dto.ProductRequestDTO
import com.questua.backend.dto.ProductResponseDTO
import com.questua.backend.model.Product

object ProductMapper {

    fun toResponse(entity: Product): ProductResponseDTO =
        ProductResponseDTO(
            id = entity.id!!,
            sku = entity.sku,
            title = entity.title,
            descriptionProduct = entity.descriptionProduct,
            priceCents = entity.priceCents,
            currency = entity.currency,
            targetType = entity.targetType,
            targetId = entity.targetId,
            createdAt = entity.createdAt
        )

    fun toEntity(dto: ProductRequestDTO): Product =
        Product(
            sku = dto.sku,
            title = dto.title,
            descriptionProduct = dto.descriptionProduct,
            priceCents = dto.priceCents,
            currency = dto.currency ?: "BRL",
            targetType = dto.targetType,
            targetId = dto.targetId
        )
}
