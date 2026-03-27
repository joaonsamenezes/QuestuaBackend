package com.questua.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import jakarta.persistence.EntityNotFoundException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class GlobalExceptionHandler {

    private fun buildResponse(status: HttpStatus, message: String?): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to status.value(),
            "error" to status.reasonPhrase,
            "message" to (message ?: "Erro desconhecido")
        )
        return ResponseEntity(body, status)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<Map<String, Any>> {
        val status = HttpStatus.valueOf(ex.statusCode.value()) 
        return buildResponse(status, ex.reason)
    }
 
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<Map<String, Any>> {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.message)
    }

    @ExceptionHandler(SecurityException::class)
    fun handleForbidden(ex: SecurityException): ResponseEntity<Map<String, Any>> {
        return buildResponse(HttpStatus.FORBIDDEN, ex.message)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFound(ex: EntityNotFoundException): ResponseEntity<Map<String, Any>> {
        return buildResponse(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Map<String, Any>> {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.")
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<Map<String, Any>> {
        val msg = "ID inválido: ${ex.value}"
        return buildResponse(HttpStatus.BAD_REQUEST, msg)
    }
    
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: org.springframework.web.bind.MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Inválido") }
        val body = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to HttpStatus.BAD_REQUEST.reasonPhrase,
            "message" to "Erro de validação",
            "errors" to errors
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }


}
  