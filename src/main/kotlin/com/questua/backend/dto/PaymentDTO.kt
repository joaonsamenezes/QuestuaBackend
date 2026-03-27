package com.questua.backend.dto
import java.util.UUID 

data class PaymentRequestDTO(
    val userId: UUID, 
    val productId: UUID,
    val amountCents: Int, 
    val currency: String = "BRL"
)


data class PaymentResponseDTO(
    val clientSecret: String, 
    val transactionId: UUID 
)