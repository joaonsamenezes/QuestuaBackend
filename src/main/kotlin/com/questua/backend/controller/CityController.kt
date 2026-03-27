package com.questua.backend.controller

import com.questua.backend.dto.CityRequestDTO
import com.questua.backend.dto.CityResponseDTO
import com.questua.backend.filter.CityFilter
import com.questua.backend.mapper.CityMapper
import com.questua.backend.service.CityService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/cities")
class CityController(private val service: CityService) { 

    @GetMapping
    fun list(
        filter: CityFilter, 
        pageable: Pageable,
        @RequestParam(required = false) userId: UUID?
    ): Page<CityResponseDTO> {
        return if (userId != null) {
            service.findForUser(userId, filter, pageable)
        } else {
            service.findAll(filter, pageable).map { CityMapper.toResponse(it) }
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): CityResponseDTO =
        CityMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: CityRequestDTO): CityResponseDTO =
        CityMapper.toResponse(service.create(CityMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: CityRequestDTO
    ): CityResponseDTO =
        CityMapper.toResponse(service.update(id, CityMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}