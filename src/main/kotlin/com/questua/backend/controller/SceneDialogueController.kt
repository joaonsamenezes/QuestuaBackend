package com.questua.backend.controller

import com.questua.backend.dto.SceneDialogueRequestDTO
import com.questua.backend.dto.SceneDialogueResponseDTO
import com.questua.backend.filter.SceneDialogueFilter
import com.questua.backend.mapper.SceneDialogueMapper
import com.questua.backend.service.SceneDialogueService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/scene-dialogues")
class SceneDialogueController(private val service: SceneDialogueService) {

    @GetMapping
    fun list(filter: SceneDialogueFilter, pageable: Pageable): Page<SceneDialogueResponseDTO> =
        service.findAll(filter, pageable).map { SceneDialogueMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): SceneDialogueResponseDTO =
        SceneDialogueMapper.toResponse(service.findById(id))

    @PostMapping
    fun create(@Valid @RequestBody dto: SceneDialogueRequestDTO): SceneDialogueResponseDTO =
        SceneDialogueMapper.toResponse(service.create(SceneDialogueMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: SceneDialogueRequestDTO
    ): SceneDialogueResponseDTO =
        SceneDialogueMapper.toResponse(service.update(id, SceneDialogueMapper.toEntity(dto)))

    @DeleteMapping("/{id}") 
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}
