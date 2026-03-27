package com.questua.backend.repository

import com.questua.backend.model.QuestPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuestPointRepository :
    JpaRepository<QuestPoint, UUID>,
    JpaSpecificationExecutor<QuestPoint>
