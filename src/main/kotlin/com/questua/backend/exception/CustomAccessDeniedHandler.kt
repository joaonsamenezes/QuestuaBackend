package com.questua.backend.exception

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val body = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.FORBIDDEN.value(),
            "error" to HttpStatus.FORBIDDEN.reasonPhrase,
            "message" to "Acesso negado"
        )
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = "application/json"
        response.writer.write(jacksonObjectMapper().writeValueAsString(body))
    }
}
 