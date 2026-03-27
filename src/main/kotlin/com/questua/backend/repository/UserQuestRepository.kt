package com.questua.backend.repository

import com.questua.backend.model.enums.ProgressStatus
import com.questua.backend.model.UserQuest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserQuestRepository : JpaRepository<UserQuest, UUID> {

    fun findByUserId(userId: UUID, pageable: Pageable): Page<UserQuest>

    fun findByQuestId(questId: UUID, pageable: Pageable): Page<UserQuest>

    // CORREÇÃO: Retorna UserQuest? (pode ser nulo) em vez de Optional
    fun findByUserIdAndQuestId(userId: UUID, questId: UUID): UserQuest? 
    
    fun countByUserIdAndQuestIdInAndProgressStatus(
            userId: UUID, 
            questIds: List<UUID>, 
            progressStatus: ProgressStatus  
        ): Long
    
    fun countByUserIdAndProgressStatus(userId: UUID, progressStatus: ProgressStatus): Long
        
    @Query("""
        SELECT COUNT(DISTINCT c.id) 
        FROM UserQuest uq
        JOIN Quest q ON uq.questId = q.id
        JOIN QuestPoint qp ON q.questPointId = qp.id
        JOIN City c ON qp.cityId = c.id
        WHERE uq.userId = :userId AND uq.progressStatus = 'COMPLETED'
    """)
    fun countDistinctCitiesCompleted(userId: UUID): Long
}