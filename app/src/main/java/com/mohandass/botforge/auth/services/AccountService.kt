// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.auth.services

import com.google.firebase.auth.AuthCredential
import com.mohandass.botforge.auth.model.User
import kotlinx.coroutines.flow.Flow

/**
 * An interface to abstract the Account Service
 */
interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val displayName: String

    val currentUser: Flow<User>

    // Randomly generate a username and set it as the display name
    suspend fun generateAndSetDisplayName()
    suspend fun setDisplayName(displayName: String)
    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun deleteAccount()
    suspend fun signOut()

    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun signInWithCredential(credential: AuthCredential)
    suspend fun linkWithCredential(credential: AuthCredential)
}
