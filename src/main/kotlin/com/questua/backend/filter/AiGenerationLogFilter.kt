package com.questua.backend.filter

import com.questua.backend.model.AiGenerationStatus
import com.questua.backend.model.AiTargetType
import java.util.UUID

data class AiGenerationLogFilter(
    val userId: UUID? = null,
    val targetType: AiTargetType? = null,
    val statusGeneration: AiGenerationStatus? = null,
    val modelName: String? = null,
    val targetId: UUID? = null
)
