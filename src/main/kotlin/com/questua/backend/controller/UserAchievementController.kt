package com.questua.backend.controller

import com.questua.backend.dto.UserAchievementRequestDTO
import com.questua.backend.dto.UserAchievementResponseDTO
import com.questua.backend.mapper.UserAchievementMapper
import com.questua.backend.service.UserAchievementService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/user-achievements")
class UserAchievementController(private val service: UserAchievementService) {

    @GetMapping
    fun list(pageable: Pageable): Page<UserAchievementResponseDTO> =
        service.findAll(pageable).map { UserAchievementMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): UserAchievementResponseDTO =
        UserAchievementMapper.toResponse(service.findById(id))

    @GetMapping("/user/{userId}")
    fun listByUser(@PathVariable userId: UUID, pageable: Pageable): Page<UserAchievementResponseDTO> =
        service.findByUser(userId, pageable).map { UserAchievementMapper.toResponse(it) }

    @GetMapping("/user/{userId}/language/{languageId}")
    fun listByUserAndLanguage(
        @PathVariable userId: UUID,
        @PathVariable languageId: UUID,
        pageable: Pageable
    ): Page<UserAchievementResponseDTO> =
        service.findByUserAndLanguage(userId, languageId, pageable)
            .map { UserAchievementMapper.toResponse(it) }

    @PostMapping
    fun create(@Valid @RequestBody dto: UserAchievementRequestDTO): UserAchievementResponseDTO =
        UserAchievementMapper.toResponse(service.create(UserAchievementMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody dto: UserAchievementRequestDTO): UserAchievementResponseDTO =
        UserAchievementMapper.toResponse(service.update(id, UserAchievementMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}
 