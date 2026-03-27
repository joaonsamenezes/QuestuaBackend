package com.questua.backend.controller

import com.questua.backend.dto.ProductRequestDTO
import com.questua.backend.dto.ProductResponseDTO
import com.questua.backend.filter.ProductFilter
import com.questua.backend.mapper.ProductMapper
import com.questua.backend.service.ProductService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/products")
class ProductController(private val service: ProductService) {

    @GetMapping
    fun list(filter: ProductFilter, pageable: Pageable): Page<ProductResponseDTO> =
        service.findAll(filter, pageable).map { ProductMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ProductResponseDTO =
        ProductMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: ProductRequestDTO): ProductResponseDTO =
        ProductMapper.toResponse(service.create(ProductMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: ProductRequestDTO
    ): ProductResponseDTO =
        ProductMapper.toResponse(service.update(id, ProductMapper.toEntity(dto)))

    @DeleteMapping("/{id}") 
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}
