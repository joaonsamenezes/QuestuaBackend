package com.questua.backend.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, unique = true)
    var sku: String,

    @Column(nullable = false)
    var title: String,

    var descriptionProduct: String? = null,

    @Column(nullable = false)
    var priceCents: Int,

    @Column(nullable = false)
    var currency: String = "BRL",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var targetType: TargetType,

    @Column(nullable = false)
    var targetId: UUID,

    @Column(nullable = false)
    var createdAt: Date = Date()
)

enum class TargetType {
    CITY, QUEST_POINT, QUEST
}
