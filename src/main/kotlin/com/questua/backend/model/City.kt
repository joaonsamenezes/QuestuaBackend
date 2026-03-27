package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.*

data class BoundingPolygon(
    val coordinates: List<List<Double>> = emptyList()
) : Serializable

@Entity
@Table(name = "city")
data class City(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, length = 100)
    var cityName: String,

    @Column(nullable = false, length = 10)
    var countryCode: String,

    @Column(nullable = false)
    var descriptionCity: String,

    @Column(nullable = false)
    var languageId: UUID,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var boundingPolygon: BoundingPolygon? = null,

    @Column(nullable = false)
    var lat: Double,

    @Column(nullable = false)
    var lon: Double,

    var imageUrl: String? = null,

    var iconUrl: String? = null,

    @Column(nullable = false)
    var isPremium: Boolean = false,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var unlockRequirement: UnlockRequirement? = null,

    @Column(nullable = false)
    var isAiGenerated: Boolean = false,

    @Column(nullable = false)
    var isPublished: Boolean = false,

    @Column(nullable = false)
    var createdAt: Date = Date()
)