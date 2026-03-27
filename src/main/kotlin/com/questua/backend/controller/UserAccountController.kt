package com.questua.backend.controller

import com.questua.backend.dto.UserAccountRequestDTO
import com.questua.backend.dto.UserAccountResponseDTO
import com.questua.backend.mapper.UserAccountMapper
import com.questua.backend.model.Language
import com.questua.backend.service.LanguageService
import com.questua.backend.service.UserAccountService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserAccountController(
    private val service: UserAccountService,
    private val languageService: LanguageService
) {

    @GetMapping
    fun list(pageable: Pageable): Page<UserAccountResponseDTO> =
        service.findAll(pageable).map { UserAccountMapper.toResponse(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): UserAccountResponseDTO =
        UserAccountMapper.toResponse(service.findById(id))

    @GetMapping("/email/{email}")
    fun getByEmail(@PathVariable email: String): UserAccountResponseDTO? =
        service.findByEmail(email)?.let { UserAccountMapper.toResponse(it) }

    @PostMapping
    fun create(@Valid @RequestBody dto: UserAccountRequestDTO): UserAccountResponseDTO {
        val lang: Language = languageService.findById(dto.nativeLanguageId)
        val saved = service.create(dto, lang)
        return UserAccountMapper.toResponse(saved)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody dto: UserAccountRequestDTO): UserAccountResponseDTO {
        val lang: Language = languageService.findById(dto.nativeLanguageId)
        val updated = service.update(id, dto, lang)
        return UserAccountMapper.toResponse(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}
 