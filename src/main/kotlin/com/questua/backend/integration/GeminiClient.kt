package com.questua.backend.integration

import com.questua.backend.dto.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GeminiClient(
    @param:Value("\${questua.ai.gemini.api-key}") private val apiKey: String,
    @param:Value("\${questua.ai.gemini.base-url}") private val baseUrl: String
) {

    private val restClient = RestClient.builder().build()

    fun generate(prompt: String): GeminiResponseDTO {
        val requestBody = GeminiRequestDTO(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt))))
        )

        return restClient.post()
            .uri("$baseUrl?key=$apiKey")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(GeminiResponseDTO::class.java)
            ?: throw RuntimeException("Resposta vazia do Gemini")
    }
} 