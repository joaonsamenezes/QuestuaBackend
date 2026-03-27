package com.questua.backend.service

import com.questua.backend.dto.UserAccountRequestDTO
import com.questua.backend.model.Language
import com.questua.backend.model.UserAccount
import com.questua.backend.repository.UserAccountRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserAccountService(
    private val repository: UserAccountRepository,
    private val encoder: PasswordEncoder,
    private val archiveStorageService: ArchiveStorageService
) {

    fun findAll(pageable: Pageable): Page<UserAccount> =
        repository.findAll(pageable)

    fun findById(id: UUID): UserAccount =
        repository.findById(id).orElseThrow { EntityNotFoundException("User not found: $id") }

    fun findByEmail(email: String): UserAccount? =
        repository.findByEmail(email.lowercase())

    @Transactional
    fun create(dto: UserAccountRequestDTO, lang: Language): UserAccount {
        val entity = UserAccount(
            email = dto.email.lowercase(),
            displayName = dto.displayName,
            passwordUser = encoder.encode(dto.password),
            avatarUrl = dto.avatarUrl,
            nativeLanguage = lang,
            userRole = dto.userRole
        )
        return repository.save(entity)
    }

    @Transactional
    fun update(id: UUID, dto: UserAccountRequestDTO, lang: Language): UserAccount {
        val existing = findById(id)

        val oldAvatar = existing.avatarUrl
        val newAvatar = dto.avatarUrl
        if (oldAvatar != null && oldAvatar != newAvatar) {
            archiveStorageService.delete(oldAvatar)
        }

        val newPassword = if (dto.password.isNullOrBlank()) {
            existing.passwordUser
        } else {
            encoder.encode(dto.password)
        }

        val merged = existing.copy(
            email = dto.email.lowercase(),
            displayName = dto.displayName,
            passwordUser = newPassword,
            avatarUrl = newAvatar,
            nativeLanguage = lang,
            userRole = dto.userRole,
            lastActiveAt = existing.lastActiveAt
        )
        return repository.save(merged)
    }

    fun delete(id: UUID) {
        val existing = findById(id)
        existing.avatarUrl?.let { archiveStorageService.delete(it) }
        repository.deleteById(id) 
    }
}