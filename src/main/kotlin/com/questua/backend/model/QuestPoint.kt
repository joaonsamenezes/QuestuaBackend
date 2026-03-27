package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "questPoint")
data class QuestPoint(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var cityId: UUID,

    @Column(nullable = false, length = 150)
    var title: String,

    @Column(nullable = false)
    var descriptionQpoint: String,

    @Column(nullable = false)
    var difficulty: Short = 1,

    @Column(nullable = false)
    var lat: Double,

    @Column(nullable = false)
    var lon: Double,

    var imageUrl: String? = null,

    var iconUrl: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var unlockRequirement: UnlockRequirement? = null,

    @Column(nullable = false)
    var isPremium: Boolean = false,

    @Column(nullable = false)
    var isAiGenerated: Boolean = false,

    @Column(nullable = false)
    var isPublished: Boolean = false,

    @Column(nullable = false)
    var createdAt: Date = Date()
)