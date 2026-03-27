package com.questua.backend.controller

import com.questua.backend.model.TransactionRecord
import com.questua.backend.model.TransactionStatus
import com.questua.backend.service.StripeService
import com.questua.backend.service.TransactionRecordService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import com.questua.backend.dto.PaymentRequestDTO
import com.questua.backend.dto.PaymentResponseDTO 

@RestController 
@RequestMapping("/api/payments")
class PaymentController(
    private val stripeService: StripeService,
    private val transactionService: TransactionRecordService
) {

    @PostMapping("/initiate")
    fun initiatePayment(@RequestBody request: PaymentRequestDTO): ResponseEntity<PaymentResponseDTO> {
       
        val paymentIntent = stripeService.createPaymentIntent(
            request.amountCents.toLong(),
            request.currency,
            request.userId,
            request.productId
        )

 
        val transaction = TransactionRecord(
            userId = request.userId,
            productId = request.productId,
            stripePaymentIntentId = paymentIntent.id, 
            amountCents = request.amountCents,
            currency = request.currency,
            statusTransaction = TransactionStatus.PENDING
        )
        
        val savedTransaction = transactionService.create(transaction)

       
        return ResponseEntity.ok(
            PaymentResponseDTO(
                clientSecret = paymentIntent.clientSecret,
                transactionId = savedTransaction.id!!
            )
        )
    }
}