package com.questua.backend.service

import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class StripeService {

    fun createPaymentIntent(amountCents: Long, currency: String, userId: UUID, productId: UUID): PaymentIntent {
        val params = PaymentIntentCreateParams.builder()
            .setAmount(amountCents)
            .setCurrency(currency.lowercase())
            .putMetadata("userId", userId.toString())
            .putMetadata("productId", productId.toString())
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
            )
            .build()

        return PaymentIntent.create(params)
    }
}