package com.questua.backend.repository

import com.questua.backend.model.AiGenerationLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AiGenerationLogRepository :
    JpaRepository<AiGenerationLog, UUID>,
    JpaSpecificationExecutor<AiGenerationLog>
 