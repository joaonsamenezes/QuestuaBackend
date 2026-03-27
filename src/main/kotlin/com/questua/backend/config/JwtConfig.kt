package com.questua.backend.config

import com.questua.backend.security.JwtUtil
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey

@Configuration
class JwtConfig {

    private val secret: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    @Bean
    fun jwtUtil(): JwtUtil = JwtUtil(secret, 1000L * 60 * 60 * 24)
}
 