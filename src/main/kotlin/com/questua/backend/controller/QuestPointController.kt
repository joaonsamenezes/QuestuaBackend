package com.questua.backend.controller

import com.questua.backend.dto.QuestPointRequestDTO
import com.questua.backend.dto.QuestPointResponseDTO
import com.questua.backend.filter.QuestPointFilter
import com.questua.backend.mapper.QuestPointMapper
import com.questua.backend.service.QuestPointService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/quest-points")
class QuestPointController(private val service: QuestPointService) {

    @GetMapping
    fun list(
        filter: QuestPointFilter, 
        pageable: Pageable,
        @RequestParam(required = false) userId: UUID?
    ): Page<QuestPointResponseDTO> {
        return if (userId != null) {
            service.findForUser(userId, filter, pageable)
        } else {
            service.findAll(filter, pageable).map { QuestPointMapper.toResponse(it) }
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): QuestPointResponseDTO =
        QuestPointMapper.toResponse(service.findById(id)) 

    @PostMapping
    fun create(@Valid @RequestBody dto: QuestPointRequestDTO): QuestPointResponseDTO =
        QuestPointMapper.toResponse(service.create(QuestPointMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: QuestPointRequestDTO
    ): QuestPointResponseDTO =
        QuestPointMapper.toResponse(service.update(id, QuestPointMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}