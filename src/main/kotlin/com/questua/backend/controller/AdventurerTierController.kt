package com.questua.backend.controller

import com.questua.backend.dto.AdventurerTierRequestDTO
import com.questua.backend.dto.AdventurerTierResponseDTO
import com.questua.backend.mapper.AdventurerTierMapper
import com.questua.backend.service.AdventurerTierService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/adventurer-tiers")
class AdventurerTierController(
    private val service: AdventurerTierService
) {
    @GetMapping
    fun list(pageable: Pageable): Page<AdventurerTierResponseDTO> =
        service.findAll(pageable).map { AdventurerTierMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): AdventurerTierResponseDTO =
        AdventurerTierMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: AdventurerTierRequestDTO): AdventurerTierResponseDTO =
        AdventurerTierMapper.toResponse(service.create(AdventurerTierMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: AdventurerTierRequestDTO
    ): AdventurerTierResponseDTO =
        AdventurerTierMapper.toResponse(service.update(id, AdventurerTierMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}