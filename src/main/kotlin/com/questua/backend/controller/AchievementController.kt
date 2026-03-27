package com.questua.backend.controller

import com.questua.backend.dto.AchievementRequestDTO
import com.questua.backend.dto.AchievementResponseDTO
import com.questua.backend.filter.AchievementFilter
import com.questua.backend.mapper.AchievementMapper
import com.questua.backend.service.AchievementService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/achievements")
class AchievementController(private val service: AchievementService) {

    @GetMapping
    fun list(filter: AchievementFilter, pageable: Pageable): Page<AchievementResponseDTO> =
        service.findAll(filter, pageable).map { AchievementMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): AchievementResponseDTO =
        AchievementMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: AchievementRequestDTO): AchievementResponseDTO =
        AchievementMapper.toResponse(service.create(AchievementMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody dto: AchievementRequestDTO): AchievementResponseDTO =
        AchievementMapper.toResponse(service.update(id, AchievementMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
} 
 