package com.mohandass.botforge.model

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.model.service.AccountService
import com.mohandass.botforge.model.service.PersonaService
import com.mohandass.botforge.model.service.implementation.AccountServiceImpl
import com.mohandass.botforge.model.service.implementation.LocalDatabase
import com.mohandass.botforge.model.service.implementation.PersonaServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideAccountService(
        auth: FirebaseAuth,
    ): AccountService = AccountServiceImpl(auth)

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application): LocalDatabase = LocalDatabase.getInstance(app)

    @Provides
    @Singleton
    fun providePersonaService(localDatabase: LocalDatabase): PersonaService = localDatabase.personaService()

    @Provides
    @Singleton
    fun providePersonaServiceImpl(personaService: PersonaService) = PersonaServiceImpl(personaService)
}
