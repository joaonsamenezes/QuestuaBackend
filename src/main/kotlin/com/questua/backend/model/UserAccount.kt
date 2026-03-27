package com.questua.backend.model

import jakarta.persistence.*
import java.util.UUID
import java.time.Instant

@Entity
@Table(name = "user_account")
data class UserAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(nullable = false, length = 100)
    var displayName: String,

    @Column(nullable = false)
    var passwordUser: String,

    var avatarUrl: String? = null,

    @ManyToOne
    @JoinColumn(name = "native_language_id", nullable = false)
    var nativeLanguage: Language,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var userRole: UserRole = UserRole.USER,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),

    var lastActiveAt: Instant? = null,
    
    @Column(name = "reset_code", length = 6)
    var resetCode: String? = null,
    
    @Column(name = "reset_code_expiry")
    var resetCodeExpiry: Instant? = null
)

enum class UserRole { USER, ADMIN }
