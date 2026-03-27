package com.questua.backend.filter

import java.util.UUID

data class CityFilter(
    val cityName: String? = null,
    val countryCode: String? = null,
    val languageId: UUID? = null,
    val isPremium: Boolean? = null,
    val isAiGenerated: Boolean? = null,
    val isPublished: Boolean? = null
)