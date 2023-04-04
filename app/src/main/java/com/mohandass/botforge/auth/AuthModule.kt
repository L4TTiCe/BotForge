package com.mohandass.botforge.auth

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.auth.services.AccountService
import com.mohandass.botforge.auth.services.implementation.AccountServiceImpl
import com.mohandass.botforge.common.services.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module to provide the authentication related services
 */
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
        logger: Logger,
        application: Application,
    ): AccountService = AccountServiceImpl(auth, logger, application)
}
