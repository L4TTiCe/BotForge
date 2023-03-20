package com.mohandass.botforge.auth.model.services.implementation

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.mohandass.botforge.auth.model.User
import com.mohandass.botforge.auth.model.services.AccountService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticate(email: String, password: String) {
        Log.v(TAG, "authenticate()")
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        Log.v(TAG, "sendRecoveryEmail()")
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun createAnonymousAccount() {
        Log.v(TAG, "createAnonymousAccount()")
        auth.signInAnonymously().await()
    }

    override suspend fun linkAccount(email: String, password: String) {
        Log.v(TAG, "linkAccount()")
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun deleteAccount() {
        Log.v(TAG, "deleteAccount()")
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()
        Log.v(TAG, "signOut()")

        // Sign the user back in anonymously.
//        createAnonymousAccount()
    }

    companion object {
        private const val TAG = "AccountServiceImpl"
    }
}
