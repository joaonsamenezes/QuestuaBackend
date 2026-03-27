package com.questua.backend.filter

data class CharacterEntityFilter(
    val nameCharacter: String? = null,
    val persona: String? = null,
    val isAiGenerated: Boolean? = null
)
