package com.questua.backend.service

import com.questua.backend.dto.GoogleSyncRequestDTO
import com.questua.backend.dto.RegisterRequestDTO
import com.questua.backend.dto.VerifyEmailRequestDTO
import com.questua.backend.model.Language
import com.questua.backend.model.PendingUser
import com.questua.backend.model.UserAccount
import com.questua.backend.model.UserLanguage
import com.questua.backend.model.UserRole
import com.questua.backend.repository.AdventurerTierRepository
import com.questua.backend.repository.PendingUserRepository
import com.questua.backend.repository.UserAccountRepository
import com.questua.backend.repository.UserLanguageRepository
import com.questua.backend.security.JwtUtil
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class AuthService(
    private val repo: UserAccountRepository,
    private val pendingRepo: PendingUserRepository,
    private val userLanguageRepo: UserLanguageRepository,
    private val adventurerTierRepo: AdventurerTierRepository,
    private val encoder: PasswordEncoder,
    private val jwt: JwtUtil,
    private val languageService: LanguageService,
    private val mailSender: JavaMailSender 
) {

    fun login(email: String, password: String): String {
        val user = repo.findByEmail(email.lowercase()) ?: throw IllegalArgumentException("invalid_credentials")
        if (!encoder.matches(password, user.passwordUser)) throw IllegalArgumentException("invalid_credentials")
        return jwt.generateToken(user.id!!, user.userRole.name)
    }

    @Transactional
    fun registerInit(dto: RegisterRequestDTO) {
        if (repo.findByEmail(dto.email.lowercase()) != null) throw IllegalArgumentException("email_exists")

        // Revertido para código de 6 dígitos
        val code = (100000..999999).random().toString()
        val hashed = encoder.encode(dto.password)
        val langId = UUID.fromString(dto.nativeLanguageId)

        var pending = pendingRepo.findByEmail(dto.email.lowercase())
        if (pending == null) {
            pending = PendingUser(
                email = dto.email.lowercase(),
                displayName = dto.displayName,
                passwordHash = hashed,
                avatarUrl = dto.avatarUrl,
                nativeLanguageId = langId,
                cefrLevel = dto.cefrLevel ?: "A1",
                verificationCode = code,
                expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES)
            )
        } else {
            pending.displayName = dto.displayName
            pending.passwordHash = hashed
            pending.avatarUrl = dto.avatarUrl
            pending.nativeLanguageId = langId
            pending.cefrLevel = dto.cefrLevel ?: "A1"
            pending.verificationCode = code
            pending.expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES)
        }
        pendingRepo.save(pending)

        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        
        val htmlContent = """
            <div style="font-family: sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px;">
                <h2 style="color: #FFC107; text-align: center;">Código de Verificação - Questua</h2>
                <p>Olá, <strong>${dto.displayName}</strong>!</p>
                <p>Sua jornada está quase começando. Use o código abaixo no aplicativo para ativar sua conta:</p>
                <div style="background-color: #f9f9f9; border: 1px dashed #FFC107; padding: 20px; text-align: center; margin: 25px 0; border-radius: 8px;">
                    <span style="font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #333;">$code</span>
                </div>
                <p style="font-size: 14px; color: #666; text-align: center;">Este código expira em 15 minutos.</p>
                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="font-size: 12px; color: #999; text-align: center;">Equipe Questua</p>
            </div>
        """.trimIndent()

        helper.setFrom("leshenlier@gmail.com")
        helper.setTo(dto.email)
        helper.setSubject("Seu código de verificação Questua")
        helper.setText(htmlContent, true)

        mailSender.send(mimeMessage)
    }

    @Transactional
    fun registerVerify(dto: VerifyEmailRequestDTO): String {
        val pending = pendingRepo.findByEmail(dto.email.lowercase()) ?: throw IllegalArgumentException("invalid_code")
        
        if (pending.verificationCode != dto.code || pending.expiresAt.isBefore(Instant.now())) {
            throw IllegalArgumentException("invalid_code")
        }

        val language = languageService.findById(pending.nativeLanguageId)

        val user = UserAccount(
            email = pending.email,
            displayName = pending.displayName,
            passwordUser = pending.passwordHash,
            avatarUrl = pending.avatarUrl,
            nativeLanguage = language,
            userRole = UserRole.USER
        )
        val savedUser = repo.save(user)

        val eligibleTier = adventurerTierRepo.findFirstByLevelRequiredLessThanEqualOrderByOrderIndexDesc(1)

        val userLanguage = UserLanguage(
            userId = savedUser.id!!,
            languageId = language.id!!,
            cefrLevel = pending.cefrLevel ?: "A1",
            adventurerTier = eligibleTier
        )
        userLanguageRepo.save(userLanguage)

        pendingRepo.delete(pending)

        return jwt.generateToken(savedUser.id!!, savedUser.userRole.name)
    }

    @Transactional
    fun syncGoogleUser(dto: GoogleSyncRequestDTO): String {
        var user = repo.findByEmail(dto.email.lowercase())
        if (user == null) {
            val langId = dto.nativeLanguageId ?: throw IllegalArgumentException("language_required_for_new_user")
            val language = languageService.findById(UUID.fromString(langId))
            val randomPassword = UUID.randomUUID().toString()
            val hashed = encoder.encode(randomPassword)
            val newUser = UserAccount(
                email = dto.email.lowercase(),
                displayName = dto.displayName,
                passwordUser = hashed,
                avatarUrl = dto.avatarUrl,
                nativeLanguage = language,
                userRole = UserRole.USER
            )
            user = repo.save(newUser)
            val eligibleTier = adventurerTierRepo.findFirstByLevelRequiredLessThanEqualOrderByOrderIndexDesc(1)
            val userLanguage = UserLanguage(
                userId = user.id!!,
                languageId = language.id!!,
                cefrLevel = dto.cefrLevel ?: "A1",
                adventurerTier = eligibleTier
            )
            userLanguageRepo.save(userLanguage)
        }
        return jwt.generateToken(user.id!!, user.userRole.name)
    }

    @Transactional
    fun forgotPassword(email: String) {
        val user = repo.findByEmail(email.lowercase()) ?: return
        val code = (100000..999999).random().toString()
        user.resetCode = code
        user.resetCodeExpiry = Instant.now().plus(15, ChronoUnit.MINUTES)
        repo.save(user)
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        val htmlContent = """
            <div style="font-family: sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px;">
                <h2 style="color: #FFC107;">Recuperação de Senha - Questua</h2>
                <p>Olá, <strong>${user.displayName}</strong>!</p>
                <div style="background-color: #f9f9f9; padding: 15px; text-align: center; border-radius: 4px; margin: 20px 0;">
                    <span style="font-size: 24px; font-weight: bold; letter-spacing: 5px; color: #333;">$code</span>
                </div>
                <p style="font-size: 14px; color: #666;">Válido por 15 minutos.</p>
                <p style="font-size: 12px; color: #999; text-align: center;">Equipe Questua</p>
            </div>
        """.trimIndent()
        helper.setFrom("leshenlier@gmail.com")
        helper.setTo(user.email)
        helper.setSubject("Recuperação de Senha - Questua")
        helper.setText(htmlContent, true)
        mailSender.send(mimeMessage)
    }

    @Transactional
    fun resetPassword(code: String, newPassword: String) {
        val user = repo.findByResetCode(code) ?: throw IllegalArgumentException("invalid_code")
        if (user.resetCodeExpiry == null || user.resetCodeExpiry!!.isBefore(Instant.now())) {
            user.resetCode = null
            user.resetCodeExpiry = null
            repo.save(user)
            throw IllegalArgumentException("expired_code")
        }
        user.passwordUser = encoder.encode(newPassword)
        user.resetCode = null
        user.resetCodeExpiry = null
        repo.save(user)
    }
}