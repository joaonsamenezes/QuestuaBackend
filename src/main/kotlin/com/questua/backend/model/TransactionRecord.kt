package com.questua.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.*

data class TransactionMetadata(
    val deviceModel: String? = null,
    val androidVersion: String? = null,
    val ipAddress: String? = null,
    val notes: String? = null
) : Serializable


@Entity
@Table(name = "transactionRecord")
data class TransactionRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var userId: UUID,

    @Column(nullable = false)
    var productId: UUID,

    @Column(nullable = false, unique = true)
    var stripePaymentIntentId: String,

    var stripeChargeId: String? = null,

    @Column(nullable = false)
    var amountCents: Int,

    @Column(nullable = false)
    var currency: String = "BRL",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var statusTransaction: TransactionStatus = TransactionStatus.PENDING,

    var receiptUrl: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var metadata: TransactionMetadata? = null,

    @Column(nullable = false)
    var createdAt: Date = Date(),

    var completedAt: Date? = null
)

enum class TransactionStatus {
    PENDING, SUCCEEDED, FAILED, REFUNDED
}
