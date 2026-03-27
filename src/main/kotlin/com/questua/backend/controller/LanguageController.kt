package com.questua.backend.controller

import com.questua.backend.dto.LanguageRequestDTO
import com.questua.backend.dto.LanguageResponseDTO
import com.questua.backend.mapper.LanguageMapper
import com.questua.backend.service.LanguageService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/languages")
class LanguageController(private val service: LanguageService) {

    @GetMapping
    fun list(
        @RequestParam(required = false) q: String?,
        pageable: Pageable
    ): Page<LanguageResponseDTO> =
        service.search(q, pageable).map { LanguageMapper.toResponse(it) } 

    @GetMapping("/{id}") 
    fun getById(@PathVariable id: UUID): LanguageResponseDTO =
        LanguageMapper.toResponse(service.findById(id))

    @GetMapping("/code/{code}")
    fun getByCode(@PathVariable code: String): LanguageResponseDTO? =
        service.findByCode(code)?.let { LanguageMapper.toResponse(it) }

    @PostMapping
    fun create(@Valid @RequestBody dto: LanguageRequestDTO): LanguageResponseDTO =
        LanguageMapper.toResponse(service.create(LanguageMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: LanguageRequestDTO
    ): LanguageResponseDTO =
        LanguageMapper.toResponse(service.update(id, LanguageMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}
 