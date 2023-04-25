// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

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

/**
 * Module to provide services related to the image generation
 */
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
