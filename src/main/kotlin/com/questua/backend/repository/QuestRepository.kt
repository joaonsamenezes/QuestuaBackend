package com.questua.backend.repository

import com.questua.backend.model.Quest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuestRepository : 
    JpaRepository<Quest, UUID>, 
    JpaSpecificationExecutor<Quest> {

    fun findByQuestPointId(questPointId: UUID, pageable: Pageable): Page<Quest>
    fun findAllByQuestPointId(questPointId: UUID): List<Quest>
} 