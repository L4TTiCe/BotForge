package com.mohandass.botforge.auth.model.services

import com.mohandass.botforge.auth.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val displayName: String

    val currentUser: Flow<User>

    suspend fun setDisplayName(displayName: String)
    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}
