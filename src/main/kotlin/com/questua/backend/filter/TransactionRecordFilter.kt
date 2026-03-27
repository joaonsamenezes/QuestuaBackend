package com.questua.backend.filter

import java.util.UUID

data class TransactionRecordFilter(
    val userId: UUID? = null,
    val productId: UUID? = null,
    val stripePaymentIntentId: String? = null,
    val status: String? = null
)