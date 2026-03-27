package com.questua.backend.service

import com.questua.backend.filter.ProductFilter
import com.questua.backend.model.Product
import com.questua.backend.repository.ProductRepository
import com.questua.backend.specification.ProductSpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service 
class ProductService(private val repository: ProductRepository) {

    fun findAll(filter: ProductFilter, pageable: Pageable): Page<Product> =
        repository.findAll(ProductSpecification.fromFilter(filter), pageable)

    fun findById(id: UUID): Product =
        repository.findById(id).orElseThrow { EntityNotFoundException("Product not found: $id") }

    fun create(entity: Product): Product =
        repository.save(entity)

    fun update(id: UUID, updated: Product): Product {
        val existing = findById(id)
        existing.sku = updated.sku
        existing.title = updated.title
        existing.descriptionProduct = updated.descriptionProduct
        existing.priceCents = updated.priceCents
        existing.currency = updated.currency
        existing.targetType = updated.targetType
        existing.targetId = updated.targetId

        return repository.save(existing)
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("Product not found: $id")
        repository.deleteById(id)
    }
}  