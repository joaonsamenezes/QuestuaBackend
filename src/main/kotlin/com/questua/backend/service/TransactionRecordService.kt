package com.questua.backend.service

import com.questua.backend.event.*
import com.questua.backend.filter.TransactionRecordFilter
import com.questua.backend.model.TransactionRecord
import com.questua.backend.model.TransactionStatus
import com.questua.backend.repository.CityRepository
import com.questua.backend.repository.ProductRepository
import com.questua.backend.repository.QuestPointRepository
import com.questua.backend.repository.TransactionRecordRepository
import com.questua.backend.specification.TransactionRecordSpecification
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransactionRecordService(
    private val repository: TransactionRecordRepository,
    private val productRepository: ProductRepository,
    private val cityRepository: CityRepository,
    private val questPointRepository: QuestPointRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun findAll(filter: TransactionRecordFilter, pageable: Pageable): Page<TransactionRecord> =
        repository.findAll(TransactionRecordSpecification.fromFilter(filter), pageable)

    fun findById(id: UUID): TransactionRecord =
        repository.findById(id).orElseThrow { EntityNotFoundException("TransactionRecord not found: $id") }

    fun create(record: TransactionRecord): TransactionRecord {
        val saved = repository.save(record)
        
        if (saved.statusTransaction == TransactionStatus.SUCCEEDED) {
            handleSuccessfulTransaction(saved)
        }

        return saved
    }

    fun update(id: UUID, updated: TransactionRecord): TransactionRecord {
        val existing = findById(id)
        
        val statusChangedToSuccess = existing.statusTransaction != TransactionStatus.SUCCEEDED && 
                                     updated.statusTransaction == TransactionStatus.SUCCEEDED

        val merged = existing.copy(
            userId = updated.userId,
            productId = updated.productId,
            stripePaymentIntentId = updated.stripePaymentIntentId,
            stripeChargeId = updated.stripeChargeId,
            amountCents = updated.amountCents,
            currency = updated.currency,
            statusTransaction = updated.statusTransaction,
            receiptUrl = updated.receiptUrl,
            metadata = updated.metadata,
            completedAt = updated.completedAt
        )
        val saved = repository.save(merged)

        if (statusChangedToSuccess) {
             handleSuccessfulTransaction(saved)
        }
        
        return saved
    }

    fun delete(id: UUID) {
        if (!repository.existsById(id)) throw EntityNotFoundException("TransactionRecord not found: $id")
        repository.deleteById(id) 
    }

    private fun handleSuccessfulTransaction(record: TransactionRecord) {
        eventPublisher.publishEvent(
            PurchaseEvent(
                userId = record.userId,
                productId = record.productId.toString(),
                amountCents = record.amountCents
            )
        )

        val product = productRepository.findById(record.productId).orElse(null) ?: return
        val targetId = product.targetId

        eventPublisher.publishEvent(PremiumContentUnlockedEvent(record.userId, targetId))

        val targetTypeStr = product.targetType.toString()

        when (targetTypeStr) {
            "CITY" -> {
                val city = cityRepository.findById(targetId).orElse(null)
                eventPublisher.publishEvent(CityUnlockedEvent(record.userId, targetId, city?.languageId))
            }
            "QUEST_POINT" -> {
                val questPoint = questPointRepository.findById(targetId).orElse(null)
                val city = questPoint?.cityId?.let { cityRepository.findById(it).orElse(null) }
                eventPublisher.publishEvent(QuestPointUnlockedEvent(record.userId, targetId, city?.languageId))
            }
            "QUEST" -> {
            }
        }
    }
}