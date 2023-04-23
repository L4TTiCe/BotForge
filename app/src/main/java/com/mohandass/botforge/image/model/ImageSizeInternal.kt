package com.mohandass.botforge.image.model

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.image.ImageSize

@OptIn(BetaOpenAI::class)
class ImageSizeInternal (size: ImageSize) {
    fun toImageSize(): ImageSize {
        return when (this) {
            is256x256 -> ImageSize.is256x256
            is512x512 -> ImageSize.is512x512
            is1024x1024 -> ImageSize.is1024x1024
            else -> {
                throw IllegalArgumentException("Unknown image size: $this")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is256x256 -> "256x256"
            is512x512 -> "512x512"
            is1024x1024 -> "1024x1024"
            else -> {
                throw IllegalArgumentException("Unknown image size: $this")
            }
        }
    }

    companion object {
        val is256x256 = ImageSizeInternal(ImageSize.is256x256)
        val is512x512 = ImageSizeInternal(ImageSize.is512x512)
        val is1024x1024 = ImageSizeInternal(ImageSize.is1024x1024)

        @OptIn(BetaOpenAI::class)
        fun from(size: ImageSize): ImageSizeInternal{
            return when (size) {
                ImageSize.is256x256 -> is256x256
                ImageSize.is512x512 -> is512x512
                ImageSize.is1024x1024 -> is1024x1024
                else -> {
                    throw IllegalArgumentException("Unknown image size: $size")
                }
            }
        }
    }
}
