package com.questua.backend.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "language")
data class Language(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, unique = true, length = 10)
    var codeLanguage: String,

    @Column(nullable = false, length = 100)
    var nameLanguage: String,

    var iconUrl: String? = null
)
