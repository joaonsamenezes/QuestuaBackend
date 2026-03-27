package com.questua.backend.mapper

import com.questua.backend.dto.TransactionRecordRequestDTO
import com.questua.backend.dto.TransactionRecordResponseDTO
import com.questua.backend.model.TransactionRecord

object TransactionRecordMapper {

    fun toResponse(entity: TransactionRecord): TransactionRecordResponseDTO =
        TransactionRecordResponseDTO(
            id = entity.id!!,
            userId = entity.userId,
            productId = entity.productId,
            stripePaymentIntentId = entity.stripePaymentIntentId,
            stripeChargeId = entity.stripeChargeId,
            amountCents = entity.amountCents,
            currency = entity.currency,
            statusTransaction = entity.statusTransaction,
            receiptUrl = entity.receiptUrl,
            metadata = entity.metadata,
            createdAt = entity.createdAt,
            completedAt = entity.completedAt
        )

    fun toEntity(dto: TransactionRecordRequestDTO): TransactionRecord =
        TransactionRecord(
            userId = dto.userId,
            productId = dto.productId,
            stripePaymentIntentId = dto.stripePaymentIntentId,
            stripeChargeId = dto.stripeChargeId,
            amountCents = dto.amountCents,
            currency = dto.currency,
            statusTransaction = dto.statusTransaction,
            receiptUrl = dto.receiptUrl,
            metadata = dto.metadata,
            completedAt = dto.completedAt
        )
}
