package com.questua.backend.controller

import com.questua.backend.dto.QuestRequestDTO
import com.questua.backend.dto.QuestResponseDTO
import com.questua.backend.mapper.QuestMapper
import com.questua.backend.service.QuestService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/quests")
class QuestController(private val service: QuestService) {

    @GetMapping
    fun getAll(pageable: Pageable): Page<QuestResponseDTO> =
        service.findAll(pageable).map { QuestMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): QuestResponseDTO =
        QuestMapper.toResponse(service.findById(id))

    @GetMapping("/point/{questPointId}")  
    fun getByQuestPoint(@PathVariable questPointId: UUID, pageable: Pageable): Page<QuestResponseDTO> =
        service.findByQuestPointId(questPointId, pageable).map { QuestMapper.toResponse(it) }

    @PostMapping
    fun create(@Valid @RequestBody dto: QuestRequestDTO): QuestResponseDTO =
        QuestMapper.toResponse(service.create(QuestMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: QuestRequestDTO
    ): QuestResponseDTO =
        QuestMapper.toResponse(service.update(id, QuestMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}  