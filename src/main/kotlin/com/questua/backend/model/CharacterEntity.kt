package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.*

data class Persona(
    val description: String? = null,
    val traits: List<String> = emptyList(),
    val speakingStyle: String? = null,
    val voiceTone: String? = null,
    val background: String? = null
) : Serializable

data class SpriteSheet(
    val urls: List<String> = emptyList()
) : Serializable

@Entity
@Table(name = "characterEntity")
data class CharacterEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, length = 100)
    var nameCharacter: String,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var persona: Persona? = null,

    @Column(nullable = false)
    var avatarUrl: String,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var spriteSheet: SpriteSheet? = null,

    var voiceUrl: String? = null,

    @Column(nullable = false)
    var isAiGenerated: Boolean = false,

    @Column(nullable = false)
    var createdAt: Date = Date()
)
