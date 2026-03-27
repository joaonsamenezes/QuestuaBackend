package com.questua.backend.model

import java.io.Serializable
import java.util.*

data class UnlockRequirement(
    val premiumAccess: Boolean = false,       
    val requiredGamificationLevel: Int? = null, 
    val requiredCefrLevel: String? = null,      
    val requiredQuests: List<UUID> = emptyList(),       
    val requiredQuestPoints: List<UUID> = emptyList()   
) : Serializable
