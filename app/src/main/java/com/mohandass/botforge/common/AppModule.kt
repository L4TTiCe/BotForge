package com.mohandass.botforge.common

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohandass.botforge.auth.model.services.AccountService
import com.mohandass.botforge.auth.model.services.implementation.AccountServiceImpl
import com.mohandass.botforge.common.service.implementation.AndroidLogger
import com.mohandass.botforge.common.service.Logger
import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.PersonaDao
import com.mohandass.botforge.chat.model.services.OpenAiService
import com.mohandass.botforge.chat.model.services.implementation.ChatServiceImpl
import com.mohandass.botforge.chat.model.services.implementation.PersonaServiceImpl
import com.mohandass.botforge.common.service.LocalDatabase
import com.mohandass.botforge.model.service.*
import com.mohandass.botforge.model.service.implementation.*
import com.mohandass.botforge.settings.model.service.PreferencesDataStore
import com.mohandass.botforge.settings.model.service.SharedPreferencesService
import com.mohandass.botforge.settings.model.service.implementation.PreferencesDataStoreImpl
import com.mohandass.botforge.settings.model.service.implementation.SharedPreferencesServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(
    name = PreferencesDataStore.PREFERENCES_NAME,
)

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
    fun provideLocalDatabase(
        app: Application
    ): LocalDatabase = LocalDatabase.getInstance(app)

    @Provides
    @Singleton
    fun providePersonaService(
        localDatabase: LocalDatabase
    ): PersonaDao = localDatabase.personaService()

    @Provides
    @Singleton
    fun providePersonaServiceImpl(
        personaDao: PersonaDao
    ) = PersonaServiceImpl(personaDao)

    @Provides
    @Singleton
    fun provideDataStore(
        app: Application
    ) = SharedPreferencesServiceImpl.getInstance(app)

    @Provides
    @Singleton
    fun provideOpenAiService(
        sharedPreferencesService: SharedPreferencesService
    ): OpenAiService = OpenAiServiceImpl.getInstance(sharedPreferencesService)

    @Provides
    @Singleton
    fun provideChatDao(
        localDatabase: LocalDatabase
    ): ChatDao = localDatabase.chatDao()

    @Provides
    @Singleton
    fun provideChatServiceImpl(
        chatDao: ChatDao
    ) = ChatServiceImpl(chatDao)

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        app: Application
    ): PreferencesDataStore = PreferencesDataStoreImpl(app.dataStore)

    @Provides
    @Singleton
    fun provideLogger(): Logger = AndroidLogger()
}