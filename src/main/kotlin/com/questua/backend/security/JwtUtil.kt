package com.questua.backend.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

class JwtUtil( 
    private val secretKey: SecretKey,
    private val expirationMs: Long
) {

    fun generateToken(userId: UUID, role: String): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): UUID {
        val body = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
        return UUID.fromString(body.subject)
    }

    fun extractRole(token: String): String {
        val body = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
        return body["role"].toString()
    }
}
 