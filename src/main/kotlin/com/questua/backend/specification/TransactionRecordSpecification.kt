package com.questua.backend.specification

import com.questua.backend.filter.TransactionRecordFilter
import com.questua.backend.model.TransactionRecord
import com.questua.backend.model.TransactionStatus
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object TransactionRecordSpecification {

    fun fromFilter(filter: TransactionRecordFilter): Specification<TransactionRecord> {
        var spec: Specification<TransactionRecord> = Specification.where(null)

        spec = spec.and(user(filter.userId))
        spec = spec.and(product(filter.productId))
        spec = spec.and(intent(filter.stripePaymentIntentId))  
        spec = spec.and(status(filter.status))

        return spec
    }

    private fun user(id: UUID?): Specification<TransactionRecord>? =
        id?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("userId"), it)
            }
        }

    private fun product(id: UUID?): Specification<TransactionRecord>? =
        id?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("productId"), it)
            }
        }

    
    private fun intent(value: String?): Specification<TransactionRecord>? =
        value?.let {
            Specification { root, _, cb ->
                cb.equal(root.get<String>("stripePaymentIntentId"), it)
            }
        }

    private fun status(value: String?): Specification<TransactionRecord>? =
        value?.let {
            try {
                val statusEnum = TransactionStatus.valueOf(it.uppercase())
                Specification { root, _, cb ->
                    cb.equal(root.get<TransactionStatus>("statusTransaction"), statusEnum)
                }
            } catch (e: IllegalArgumentException) {
                null 
            }
        }
}