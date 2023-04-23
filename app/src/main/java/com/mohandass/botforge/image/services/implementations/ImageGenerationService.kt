package com.mohandass.botforge.image.services.implementations

import com.mohandass.botforge.image.model.dao.ImageGenerationDao
import com.mohandass.botforge.common.services.Logger
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE
import com.mohandass.botforge.image.model.dao.entities.relations.ImageGenerationRequestWithImages

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
        images: List<GeneratedImageE>
    ) {
        logger.logVerbose(TAG, "saveImageGenerationRequestAndImages()")
        saveImageGenerationRequest(request)
        images.forEach { saveGeneratedImage(it) }
    }

    suspend fun getPastImageGenerations(): List<ImageGenerationRequestWithImages> {
        logger.logVerbose(TAG, "getPastImageGenerations()")
        return imageGenerationDao.getAllGeneratedImages()
    }

    companion object {
        private const val TAG = "ImageGenerationService"
    }
}