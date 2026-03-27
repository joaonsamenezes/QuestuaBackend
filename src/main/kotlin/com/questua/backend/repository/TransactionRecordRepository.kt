package com.questua.backend.repository

import com.questua.backend.model.TransactionRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.Optional 

@Repository
interface TransactionRecordRepository :
    JpaRepository<TransactionRecord, UUID>,
    JpaSpecificationExecutor<TransactionRecord> {
    fun findByStripePaymentIntentId(stripePaymentIntentId: String): Optional<TransactionRecord>
}