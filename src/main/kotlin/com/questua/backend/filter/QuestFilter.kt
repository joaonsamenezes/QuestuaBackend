package com.questua.backend.filter

import java.util.UUID

data class QuestFilter(
    val title: String? = null,
    val questPointId: UUID? = null,
    val difficulty: Int? = null,
    val isPremium: Boolean? = null,
    val isAiGenerated: Boolean? = null,
    val isPublished: Boolean? = null
)