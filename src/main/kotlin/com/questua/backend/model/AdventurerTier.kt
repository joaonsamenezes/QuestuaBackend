package com.questua.backend.model

import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "adventurer_tier")
data class AdventurerTier(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "key_name", unique = true, nullable = false, length = 50)
    var keyName: String,

    @Column(name = "name_display", nullable = false, length = 100)
    var nameDisplay: String,

    @Column(name = "icon_url", columnDefinition = "TEXT")
    var iconUrl: String? = null,

    @Column(name = "color_hex", length = 7)
    var colorHex: String? = null,

    @Column(name = "order_index", nullable = false)
    var orderIndex: Int,

    @Column(name = "level_required", nullable = false)
    var levelRequired: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Date = Date()
)