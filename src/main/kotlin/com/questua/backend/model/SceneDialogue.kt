package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.*

data class CharacterState(
    val characterId: UUID,
    val expression: String?, 
    val position: String?
) : Serializable

data class SceneEffect(
    val type: String,
    val intensity: Double?,
    val duration: Double?,
    val soundUrl: String?  
) : Serializable

data class Choice(
    val text: String,
    val nextDialogueId: UUID?
) : Serializable

@Entity
@Table(name = "sceneDialogue")
data class SceneDialogue(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var descriptionDialogue: String,

    @Column(nullable = false)
    var backgroundUrl: String,

    var bgMusicUrl: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var characterStates: List<CharacterState>? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var sceneEffects: List<SceneEffect>? = null,

    var speakerCharacterId: UUID? = null,

    @Column(nullable = false)
    var textContent: String,

    var audioUrl: String? = null,

    @Column(nullable = false)
    var expectsUserResponse: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var inputMode: InputMode = InputMode.NONE,

    var expectedResponse: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var choices: List<Choice>? = null,

    var nextDialogueId: UUID? = null,

    @Column(nullable = false)
    var isAiGenerated: Boolean = false,

    @Column(nullable = false)
    var createdAt: Date = Date()
)

enum class InputMode {
    TEXT, CHOICE, NONE
}
