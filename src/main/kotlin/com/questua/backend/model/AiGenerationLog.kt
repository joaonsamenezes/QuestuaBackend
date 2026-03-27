package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.*

data class AiGenerationResponseMeta(
    val tokensUsed: Int?,
    val durationSeconds: Double?,
    val latencyMs: Long? = null,
    val temperature: Double? = null,
    val reasoning: String? = null,
    val extraInfo: String?
) : Serializable

@Entity
@Table(name = "ai_generation_log")
data class AiGenerationLog(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    var userId: UUID? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var targetType: AiTargetType, 
 
    var targetId: UUID? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var prompt: String,

    @Column(nullable = false)
    var modelName: String, 
    
    @Column(columnDefinition = "TEXT")
    var responseText: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var responseMeta: AiGenerationResponseMeta? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var statusGeneration: AiGenerationStatus = AiGenerationStatus.SUCCESS,

    @Column(nullable = false)
    var createdAt: Date = Date()
)

enum class AiTargetType {
    QUEST, QUEST_POINT, SCENE_DIALOGUE, CHARACTER, ACHIEVEMENT
}

enum class AiGenerationStatus {
    SUCCESS, ERROR, TIMEOUT
}
