package com.questua.backend.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "pending_users")
data class PendingUser(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(nullable = false)
    var displayName: String,

    @Column(nullable = false)
    var passwordHash: String,

    var avatarUrl: String? = null,

    @Column(nullable = false)
    var nativeLanguageId: UUID,

    var cefrLevel: String? = "A1",

    @Column(nullable = false)
    var verificationCode: String,

    @Column(nullable = false)
    var expiresAt: Instant
)