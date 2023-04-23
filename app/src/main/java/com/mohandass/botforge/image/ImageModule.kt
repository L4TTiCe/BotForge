package com.mohandass.botforge.image

import com.mohandass.botforge.common.services.LocalDatabase
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.image.model.dao.ImageGenerationDao
import com.mohandass.botforge.image.services.implementations.ImageGenerationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ImageModule {
    @Provides
    @Singleton
    fun provideImageGenerationDao(
        localDatabase: LocalDatabase
    ): ImageGenerationDao = localDatabase.imageDao()

    @Provides
    @Singleton
    fun provideImageGenerationServiceImpl(
        imageGenerationDao: ImageGenerationDao,
        logger: Logger,
    ) = ImageGenerationService(imageGenerationDao, logger)
}
