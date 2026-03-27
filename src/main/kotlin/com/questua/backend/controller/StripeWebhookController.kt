package com.questua.backend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.questua.backend.model.*
import com.questua.backend.repository.ProductRepository
import com.questua.backend.repository.TransactionRecordRepository
import com.questua.backend.service.UnlockService
import com.stripe.net.Webhook
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/api/webhooks/stripe")
class StripeWebhookController(
    @Value("\${stripe.webhook-secret}") private val webhookSecret: String, 
    private val transactionRecordRepository: TransactionRecordRepository,
    private val productRepository: ProductRepository,
    private val unlockService: UnlockService,
    private val objectMapper: ObjectMapper
) {

    @PostMapping  
    fun handleWebhook(
        @RequestBody payload: String,
        @RequestHeader("Stripe-Signature") sigHeader: String
    ): ResponseEntity<String> {

        val event = try {
            Webhook.constructEvent(payload, sigHeader, webhookSecret)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }

        val stripeId = try {
            val rawJson = event.dataObjectDeserializer.rawJson
            val node = objectMapper.readTree(rawJson)
            if (node.has("id")) {
                node.get("id").asText()
            } else {
                return ResponseEntity.ok("Ignored")
            }
        } catch (e: Exception) {
            return ResponseEntity.ok("Ignored")
        }

        if (event.type == "payment_intent.succeeded") {
            updateTransaction(stripeId, TransactionStatus.SUCCEEDED)
        } else if (event.type == "payment_intent.payment_failed") {
            updateTransaction(stripeId, TransactionStatus.FAILED)
        }

        return ResponseEntity.ok("Received")
    }

    private fun updateTransaction(stripeId: String, status: TransactionStatus) {
        val transactionOpt = transactionRecordRepository.findByStripePaymentIntentId(stripeId)

        if (transactionOpt.isPresent) {
            val transaction = transactionOpt.get()
            transaction.statusTransaction = status
            
            if (status == TransactionStatus.SUCCEEDED) {
                transaction.completedAt = Date()
                unlockService.unlockContentAfterPayment(transaction.userId, transaction.productId)
            }
            
            transactionRecordRepository.save(transaction)
        }
    }
}