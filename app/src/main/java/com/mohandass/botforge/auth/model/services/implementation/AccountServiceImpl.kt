package com.mohandass.botforge.auth.model.services.implementation

import android.app.Application
import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.mohandass.botforge.R
import com.mohandass.botforge.auth.model.User
import com.mohandass.botforge.auth.model.services.AccountService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    application: Application,
) : AccountService {

    private val adjectives =
        application.applicationContext.resources.getStringArray(R.array.adjectives)
    private val animals = application.applicationContext.resources.getStringArray(R.array.animals)

    private fun generateUsername() = "${adjectives.random()}${animals.random()}"

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val displayName: String
        get() = auth.currentUser?.displayName.orEmpty()

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let {
                        User(
                            it.uid,
                            it.isAnonymous,
                            it.displayName
                        )
                    } ?: User())
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

    override suspend fun setDisplayName(displayName: String) {
        Log.v(TAG, "setDisplayName()")
        auth.currentUser!!.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
        ).await()
    }

    override suspend fun generateAndSetDisplayName() {
        Log.v(TAG, "generateAndSetDisplayName()")
        setDisplayName(generateUsername())
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

    override suspend fun createAnonymousAccount() {
        Log.v(TAG, "createAnonymousAccount()")
        auth.signInAnonymously().await()
        setDisplayName(generateUsername())
    }

    override suspend fun linkAccount(email: String, password: String) {
        Log.v(TAG, "linkAccount()")
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun signInWithCredential(credential: AuthCredential) {
        Log.v(TAG, "googleSignIn()")
        auth.signInWithCredential(credential).await()
    }

    override suspend fun linkWithCredential(credential: AuthCredential) {
        Log.v(TAG, "linkWithCredential()")
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    companion object {
        private const val TAG = "AccountServiceImpl"
    }
}
