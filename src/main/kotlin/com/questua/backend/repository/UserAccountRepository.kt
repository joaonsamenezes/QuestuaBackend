package com.questua.backend.repository

import com.questua.backend.model.UserAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserAccountRepository : JpaRepository<UserAccount, UUID> {
    fun findByEmail(email: String): UserAccount?
    fun findByResetCode(resetCode: String): UserAccount?
}
