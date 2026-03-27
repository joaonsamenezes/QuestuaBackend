package com.questua.backend.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "userAchievement", uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "achievementId", "languageId"])])
data class UserAchievement(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var userId: UUID,

    @Column(nullable = false)
    var achievementId: UUID,

    var languageId: UUID? = null,

    @Column(nullable = false)
    var awardedAt: Date = Date()
)
