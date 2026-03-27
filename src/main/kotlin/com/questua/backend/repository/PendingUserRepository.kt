package com.questua.backend.repository

import com.questua.backend.model.PendingUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PendingUserRepository : JpaRepository<PendingUser, UUID> {
    fun findByEmail(email: String): PendingUser?
}