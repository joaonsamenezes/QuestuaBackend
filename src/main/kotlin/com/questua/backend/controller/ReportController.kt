package com.questua.backend.controller

import com.questua.backend.dto.ReportRequestDTO
import com.questua.backend.dto.ReportResponseDTO
import com.questua.backend.filter.ReportFilter
import com.questua.backend.mapper.ReportMapper
import com.questua.backend.service.ReportService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/reports")
class ReportController(private val service: ReportService) {

    @GetMapping
    fun list(filter: ReportFilter, pageable: Pageable): Page<ReportResponseDTO> =
        service.findAll(filter, pageable).map { ReportMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ReportResponseDTO =
        ReportMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: ReportRequestDTO): ReportResponseDTO =
        ReportMapper.toResponse(service.create(ReportMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: ReportRequestDTO
    ): ReportResponseDTO =
        ReportMapper.toResponse(service.update(id, ReportMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    } 
}
