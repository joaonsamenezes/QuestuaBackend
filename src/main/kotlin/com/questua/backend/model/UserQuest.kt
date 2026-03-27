package com.questua.backend.model

import com.questua.backend.model.enums.ProgressStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "user_quest")
data class UserQuest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(name = "quest_id", nullable = false)
    var questId: UUID,

    @Column(name = "last_dialogue_id")
    var lastDialogueId: UUID? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_status", nullable = false)
    var progressStatus: ProgressStatus = ProgressStatus.IN_PROGRESS,

    @Column(name = "xp_earned")
    var xpEarned: Int = 0,

    @Column(name = "score")
    var score: Int? = 0,

    @Column(name = "percent_complete")
    var percentComplete: Double = 0.0,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var responses: List<Response>? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var overallAssessment: List<SkillAssessment>? = null,

    @Column(name = "started_at")
    var startedAt: Date? = null,

    @Column(name = "completed_at")
    var completedAt: Date? = null,

    @Column(name = "last_activity_at")
    var lastActivityAt: Date? = null
)

data class Response(
    var questionId: UUID,
    var answer: String,
    var correct: Boolean?,
    var feedback: String?
)

data class SkillAssessment(
    var skill: String,
    var score: Int,
    var feedback: String?
)