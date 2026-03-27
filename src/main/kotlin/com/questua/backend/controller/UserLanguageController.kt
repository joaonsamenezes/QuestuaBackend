package com.questua.backend.controller

import com.questua.backend.dto.UserLanguageRequestDTO
import com.questua.backend.dto.UserLanguageResponseDTO
import com.questua.backend.mapper.UserLanguageMapper
import com.questua.backend.service.UserLanguageService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/user-languages")
class UserLanguageController(private val service: UserLanguageService) {

    @GetMapping
    fun list(pageable: Pageable): Page<UserLanguageResponseDTO> =
        service.findAll(pageable).map { UserLanguageMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): UserLanguageResponseDTO =
        UserLanguageMapper.toResponse(service.findById(id)) 

    @GetMapping("/user/{userId}")
    fun getByUserId(
        @PathVariable userId: UUID,
        pageable: Pageable
    ): Page<UserLanguageResponseDTO> =
        service.findByUserId(userId, pageable).map { UserLanguageMapper.toResponse(it) }

    @GetMapping("/language/{languageId}")
    fun getByLanguageId(
        @PathVariable languageId: UUID,
        pageable: Pageable
    ): Page<UserLanguageResponseDTO> =
        service.findByLanguageId(languageId, pageable).map { UserLanguageMapper.toResponse(it) }

    @GetMapping("/leaderboard")
    fun getLeaderboard(
        @RequestParam adventurerTierId: UUID,
        @RequestParam cefrLevel: String,
        pageable: Pageable
    ): Page<UserLanguageResponseDTO> =
        service.getLeaderboard(adventurerTierId, cefrLevel, pageable).map { UserLanguageMapper.toResponse(it) }

    @PostMapping
    fun create(@Valid @RequestBody dto: UserLanguageRequestDTO): UserLanguageResponseDTO =
        UserLanguageMapper.toResponse(service.create(UserLanguageMapper.toEntity(dto)))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: UserLanguageRequestDTO
    ): UserLanguageResponseDTO =
        UserLanguageMapper.toResponse(service.update(id, UserLanguageMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}