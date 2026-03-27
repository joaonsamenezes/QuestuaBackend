package com.questua.backend.filter

import java.util.UUID

data class SceneDialogueFilter(
    val speakerCharacterId: UUID? = null,
    val text: String? = null
)
