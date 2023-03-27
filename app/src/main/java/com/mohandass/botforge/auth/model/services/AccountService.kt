package com.mohandass.botforge.auth.model.services

import com.google.firebase.auth.AuthCredential
import com.mohandass.botforge.auth.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val displayName: String

    val currentUser: Flow<User>

    suspend fun generateAndSetDisplayName()
    suspend fun setDisplayName(displayName: String)
    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun deleteAccount()
    suspend fun signOut()

    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun signInWithCredential(credential: AuthCredential)
}
