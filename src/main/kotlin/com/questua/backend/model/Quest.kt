package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.*

data class LearningFocus(
    val grammarTopics: List<String>? = null,
    val vocabularyThemes: List<String>? = null,
    val skills: List<String>? = null
) : Serializable

@Entity
@Table(name = "quest")
data class Quest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var questPointId: UUID,

    var firstDialogueId: UUID? = null,

    @Column(nullable = false, length = 150)
    var title: String,

    @Column(nullable = false)
    var descriptionQuest: String,

    @Column(nullable = false)
    var difficulty: Short = 1,

    @Column(nullable = false)
    var orderIndex: Short = 1,

    @Column(nullable = false)
    var xpValue: Int = 0, 

    @Column(nullable = false)
    var xpPerQuestion: Int = 10, 

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var unlockRequirement: UnlockRequirement? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var learningFocus: LearningFocus? = null,

    @Column(nullable = false)
    var isPremium: Boolean = false,

    @Column(nullable = false)
    var isAiGenerated: Boolean = false,

    @Column(nullable = false)
    var isPublished: Boolean = false,

    @Column(nullable = false)
    var createdAt: Date = Date()
)