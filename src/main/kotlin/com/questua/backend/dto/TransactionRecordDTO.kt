package com.questua.backend.dto

import com.questua.backend.model.TransactionMetadata
import com.questua.backend.model.TransactionStatus
import jakarta.validation.constraints.*
import java.util.*

data class TransactionRecordRequestDTO(
    @field:NotNull(message = "userId é obrigatório")
    val userId: UUID,

    @field:NotNull(message = "productId é obrigatório")
    val productId: UUID,

    @field:NotBlank(message = "stripePaymentIntentId não pode ser vazio")
    @field:Size(max = 100, message = "stripePaymentIntentId deve ter no máximo 100 caracteres")
    val stripePaymentIntentId: String,

    @field:Size(max = 100, message = "stripeChargeId deve ter no máximo 100 caracteres")
    val stripeChargeId: String? = null,

    @field:Min(value = 0, message = "amountCents não pode ser negativo")
    val amountCents: Int,

    @field:Size(max = 5, message = "currency deve ter no máximo 5 caracteres")
    val currency: String = "BRL",

    @field:NotNull(message = "statusTransaction é obrigatório")
    val statusTransaction: TransactionStatus = TransactionStatus.PENDING,

    @field:Size(max = 255, message = "receiptUrl deve ter no máximo 255 caracteres")
    val receiptUrl: String? = null,

    val metadata: TransactionMetadata? = null,
    val completedAt: Date? = null
)

data class TransactionRecordResponseDTO(
    val id: UUID,
    val userId: UUID,
    val productId: UUID,
    val stripePaymentIntentId: String,
    val stripeChargeId: String?,
    val amountCents: Int,
    val currency: String,
    val statusTransaction: TransactionStatus,
    val receiptUrl: String?,
    val metadata: TransactionMetadata?,
    val createdAt: Date,
    val completedAt: Date?
)
 