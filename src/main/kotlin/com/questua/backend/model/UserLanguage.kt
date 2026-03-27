package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.*

data class UnlockedContent(
    val cities: List<String> = emptyList(),
    val questPoints: List<String> = emptyList(),
    val quests: List<String> = emptyList()
) : Serializable

@Entity
@Table(name = "userLanguage")
data class UserLanguage(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var userId: UUID,

    @Column(nullable = false)
    var languageId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var statusLanguage: StatusLanguage = StatusLanguage.ACTIVE,

    @Column(nullable = false, length = 2)
    var cefrLevel: String = "A1",

    @Column(nullable = false)
    var questsTowardsNextLevel: Int = 0,

    @Column(nullable = false)
    var gamificationLevel: Int = 1,

    @Column(nullable = false)
    var xpTotal: Int = 0,

    @Column(nullable = false)
    var streakDays: Int = 0,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var unlockedContent: UnlockedContent? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adventurer_tier_id")
    var adventurerTier: AdventurerTier? = null,

    @Column(nullable = false)
    var startedAt: Date = Date(),

    var lastActiveAt: Date? = null
)

enum class StatusLanguage {
    ACTIVE,
    ABANDONED,
    RESUMED
}