package com.questua.backend.controller

import com.questua.backend.dto.*
import com.questua.backend.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val auth: AuthService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody dto: LoginRequestDTO): LoginResponseDTO {
        val token = auth.login(dto.email, dto.password)
        return LoginResponseDTO(token)
    }

    @PostMapping("/register/init")
    fun registerInit(@Valid @RequestBody dto: RegisterRequestDTO) {
        auth.registerInit(dto)
    }

    @PostMapping("/register/verify")
    fun registerVerify(@Valid @RequestBody dto: VerifyEmailRequestDTO): LoginResponseDTO {
        val token = auth.registerVerify(dto)
        return LoginResponseDTO(token)
    }

    @PostMapping("/google-sync")
    fun syncGoogleUser(@Valid @RequestBody dto: GoogleSyncRequestDTO): LoginResponseDTO {
        val token = auth.syncGoogleUser(dto)
        return LoginResponseDTO(token)
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@Valid @RequestBody dto: ForgotPasswordRequestDTO) {
        auth.forgotPassword(dto.email)
    }

    @PostMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody dto: ResetPasswordRequestDTO) {
        auth.resetPassword(dto.code, dto.newPassword)
    }
}