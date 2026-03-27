package com.questua.backend.filter

import com.questua.backend.model.TargetType 
import java.util.UUID

data class ProductFilter(
    val sku: String? = null,
    val title: String? = null,
    val targetType: TargetType? = null, 
    val targetId: UUID? = null, 
    val currency: String? = null 
)
