package com.questua.backend.config

import com.stripe.Stripe
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class StripeConfig(
    @param:Value("\${stripe.api-key}") private val apiKey: String
) {
    @PostConstruct
    fun init() {
        Stripe.apiKey = apiKey
    }
}  