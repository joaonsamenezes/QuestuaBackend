package com.questua.backend.filter

import com.questua.backend.model.enums.RarityType

data class AchievementFilter(
    val keyName: String? = null,
    val rarity: RarityType? = null,
    val minXp: Int? = null,
    val maxXp: Int? = null
)
 