package com.questua.backend.controller

import com.questua.backend.dto.SubmitResponseRequestDTO
import com.questua.backend.dto.SubmitResponseResultDTO
import com.questua.backend.dto.UserQuestRequestDTO
import com.questua.backend.dto.UserQuestResponseDTO
import com.questua.backend.mapper.UserQuestMapper
import com.questua.backend.service.UserQuestService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/user-quests")
class UserQuestController(private val service: UserQuestService) {

    @GetMapping
    fun getAll(pageable: Pageable): Page<UserQuestResponseDTO> =
        service.findAll(pageable).map { UserQuestMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): UserQuestResponseDTO =
        UserQuestMapper.toResponse(service.findById(id))

    @GetMapping("/user/{userId}")
    fun getByUser(@PathVariable userId: UUID, pageable: Pageable): Page<UserQuestResponseDTO> =
        service.findByUser(userId, pageable).map { UserQuestMapper.toResponse(it) }

    @GetMapping("/quest/{questId}")
    fun getByQuest(@PathVariable questId: UUID, pageable: Pageable): Page<UserQuestResponseDTO> =
        service.findByQuest(questId, pageable).map { UserQuestMapper.toResponse(it) }

    @GetMapping("/user/{userId}/quest/{questId}")
    fun getByUserAndQuest(
        @PathVariable userId: UUID,
        @PathVariable questId: UUID
    ): ResponseEntity<UserQuestResponseDTO> {
        val userQuest = service.findByUserAndQuest(userId, questId)
        return if (userQuest != null) {
            ResponseEntity.ok(UserQuestMapper.toResponse(userQuest))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun create(@Valid @RequestBody dto: UserQuestRequestDTO): UserQuestResponseDTO =
        UserQuestMapper.toResponse(service.create(UserQuestMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: UserQuestRequestDTO
    ): UserQuestResponseDTO =
        UserQuestMapper.toResponse(service.update(id, UserQuestMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }

    @PostMapping("/{id}/submit")
    fun submitResponse(
        @PathVariable id: UUID,
        @Valid @RequestBody request: SubmitResponseRequestDTO
    ): ResponseEntity<SubmitResponseResultDTO> {
        val result = service.submitResponse(id, request)
        return ResponseEntity.ok(result)
    }
}