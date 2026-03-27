package com.questua.backend.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class GeminiRequestDTO(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig = GeminiGenerationConfig()
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiGenerationConfig(
    val temperature: Double = 0.7,
    val responseMimeType: String = "application/json"
)

data class GeminiResponseDTO(
    val candidates: List<GeminiCandidate>?,
    val usageMetadata: GeminiUsageMetadata?
)

data class GeminiCandidate(
    val content: GeminiContent,
    val finishReason: String?
)

data class GeminiUsageMetadata(
    val promptTokenCount: Int,
    val candidatesTokenCount: Int,
    val totalTokenCount: Int
)


open class BaseAiGenerationRequestDTO(
    val targetLanguage: String = "PT",
    val modelName: String = "gemini-2.0-flash"
)

data class GenerateQuestPointRequestDTO(
    val cityId: UUID,
    val theme: String
) : BaseAiGenerationRequestDTO()

data class GenerateQuestRequestDTO(
    val questPointId: UUID,
    val context: String,
    val difficultyLevel: Int = 1
) : BaseAiGenerationRequestDTO()

data class GenerateCharacterRequestDTO(
    val archetype: String
) : BaseAiGenerationRequestDTO()

data class GenerateDialogueRequestDTO(
    val questId: UUID? = null,
    val speakerCharacterId: UUID,
    val context: String,
    val inputMode: String = "CHOICE"
) : BaseAiGenerationRequestDTO()

data class GenerateAchievementRequestDTO(
    val triggerAction: String,
    val difficulty: String
) : BaseAiGenerationRequestDTO()