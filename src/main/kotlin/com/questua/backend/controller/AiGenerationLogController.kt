package com.questua.backend.controller

import com.questua.backend.dto.AiGenerationLogRequestDTO
import com.questua.backend.dto.AiGenerationLogResponseDTO
import com.questua.backend.filter.AiGenerationLogFilter
import com.questua.backend.mapper.AiGenerationLogMapper
import com.questua.backend.service.AiGenerationLogService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/ai-generation-logs")
class AiGenerationLogController(private val service: AiGenerationLogService) {

    @GetMapping
    fun list(filter: AiGenerationLogFilter, pageable: Pageable): Page<AiGenerationLogResponseDTO> =
        service.findAll(filter, pageable).map { AiGenerationLogMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): AiGenerationLogResponseDTO =
        AiGenerationLogMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: AiGenerationLogRequestDTO): AiGenerationLogResponseDTO =
        AiGenerationLogMapper.toResponse(service.create(AiGenerationLogMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: AiGenerationLogRequestDTO
    ): AiGenerationLogResponseDTO =
        AiGenerationLogMapper.toResponse(service.update(id, AiGenerationLogMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
} 
