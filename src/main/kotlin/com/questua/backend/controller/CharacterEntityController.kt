package com.questua.backend.controller

import com.questua.backend.dto.CharacterEntityRequestDTO
import com.questua.backend.dto.CharacterEntityResponseDTO
import com.questua.backend.filter.CharacterEntityFilter
import com.questua.backend.mapper.CharacterEntityMapper
import com.questua.backend.service.CharacterEntityService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/characters")
class CharacterEntityController(private val service: CharacterEntityService) {

    @GetMapping
    fun list(filter: CharacterEntityFilter, pageable: Pageable): Page<CharacterEntityResponseDTO> =
        service.findAll(filter, pageable).map { CharacterEntityMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): CharacterEntityResponseDTO =
        CharacterEntityMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: CharacterEntityRequestDTO): CharacterEntityResponseDTO =
        CharacterEntityMapper.toResponse(service.create(CharacterEntityMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: CharacterEntityRequestDTO
    ): CharacterEntityResponseDTO =
        CharacterEntityMapper.toResponse(service.update(id, CharacterEntityMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) { 
        service.delete(id)
    }
}
