// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.image.services.implementations

import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.image.model.dao.ImageGenerationDao
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE
import com.mohandass.botforge.image.model.dao.entities.relations.ImageGenerationRequestWithImages

/**
 * A service to handle ImageGenerationRequest and GeneratedImage entities
 *
 * This service is used to handle the ImageGenerationRequest and GeneratedImage entities
 */
class ImageGenerationService(
    private val imageGenerationDao: ImageGenerationDao,
    private val logger: Logger
) {
    private suspend fun saveImageGenerationRequest(request: ImageGenerationRequestE) {
        logger.logVerbose(TAG, "saveImageGenerationRequest() Saving request: $request")
        imageGenerationDao.insertImageGenerationRequest(request)
    }

    private suspend fun saveGeneratedImage(image: GeneratedImageE) {
        logger.logVerbose(TAG, "saveGeneratedImage() Saving image: $image")
        imageGenerationDao.insertGeneratedImage(image)
    }

    suspend fun saveImageGenerationRequestAndImages(
        request: ImageGenerationRequestE,
        images: List<GeneratedImageE>,
        onSuccess: () -> Unit = {}
    ) {
        logger.logVerbose(TAG, "saveImageGenerationRequestAndImages()")
        saveImageGenerationRequest(request)
        images.forEach { saveGeneratedImage(it) }
        onSuccess()
    }

    suspend fun getPastImageGenerations(): List<ImageGenerationRequestWithImages> {
        logger.logVerbose(TAG, "getPastImageGenerations()")
        return imageGenerationDao.getAllGeneratedImages()
    }

    suspend fun deleteImageGenerationRequestById(requestId: String) {
        logger.logVerbose(TAG, "deleteImageGenerationRequest()")
        imageGenerationDao.deleteImageGenerationRequestById(requestId)
    }

    suspend fun deleteAllImageGenerationRequests() {
        logger.logVerbose(TAG, "deleteAllImageGenerationRequests()")
        imageGenerationDao.deleteAllImageGenerationRequests()
    }

    companion object {
        private const val TAG = "ImageGenerationService"
    }
}