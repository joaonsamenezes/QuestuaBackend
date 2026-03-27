package com.questua.backend.repository

import com.questua.backend.model.Achievement
import com.questua.backend.model.enums.AchievementConditionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AchievementRepository : JpaRepository<Achievement, UUID>, JpaSpecificationExecutor<Achievement> {

    @Query("SELECT a FROM Achievement a WHERE a.conditionType = :conditionType AND (a.languageId = :languageId OR a.isGlobal = true)")
    fun findEligibleAchievements(
        @Param("conditionType") conditionType: AchievementConditionType,
        @Param("languageId") languageId: UUID?
    ): List<Achievement>

    @Query("SELECT a FROM Achievement a WHERE a.conditionType = :conditionType AND a.targetId = :targetId AND (a.languageId = :languageId OR a.isGlobal = true)")
    fun findEligibleAchievementsWithTarget(
        @Param("conditionType") conditionType: AchievementConditionType,
        @Param("targetId") targetId: UUID,
        @Param("languageId") languageId: UUID?
    ): List<Achievement>
}