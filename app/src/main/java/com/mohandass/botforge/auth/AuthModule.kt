package com.mohandass.botforge.auth

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.auth.model.services.implementation.AccountServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideAccountService(
        auth: FirebaseAuth,
        application: Application,
    ): AccountService = AccountServiceImpl(auth, application)
}
