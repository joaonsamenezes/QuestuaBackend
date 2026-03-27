package com.questua.backend.repository

import com.questua.backend.model.Language
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LanguageRepository : JpaRepository<Language, UUID> {

    fun findByCodeLanguage(codeLanguage: String): Language?

    fun findByNameLanguageContainingIgnoreCase(nameLanguage: String, pageable: Pageable): Page<Language>

    @Query("""
        SELECT l FROM Language l 
        WHERE LOWER(l.codeLanguage) LIKE LOWER(CONCAT('%', :filter, '%')) 
        OR LOWER(l.nameLanguage) LIKE LOWER(CONCAT('%', :filter, '%'))
    """)
    fun searchByCodeOrName(filter: String, pageable: Pageable): Page<Language>
}
 