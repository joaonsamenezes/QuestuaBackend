package com.questua.backend.repository

import com.questua.backend.model.UserLanguage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserLanguageRepository : JpaRepository<UserLanguage, UUID> {

    fun findByUserId(userId: UUID, pageable: Pageable): Page<UserLanguage>
    
    fun findByLanguageId(languageId: UUID, pageable: Pageable): Page<UserLanguage>

    fun findByUserId(userId: UUID): List<UserLanguage>

    fun findByUserIdAndLanguageId(userId: UUID, languageId: UUID): Optional<UserLanguage>

    fun findByAdventurerTierIdAndCefrLevelOrderByXpTotalDesc(adventurerTierId: UUID, cefrLevel: String, pageable: Pageable): Page<UserLanguage>
}