package com.questua.backend.filter

import java.util.UUID

data class QuestPointFilter(
    val cityId: UUID? = null,
    val title: String? = null,
    val difficulty: Short? = null,
    val isPremium: Boolean? = null,
    val isAiGenerated: Boolean? = null,
    val isPublished: Boolean? = null
)