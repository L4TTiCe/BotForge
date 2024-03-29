// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.image.model.dao.entities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.mohandass.botforge.image.model.ImageSizeInternal
import java.io.ByteArrayOutputStream

/**
 * A class to convert custom types to and from their database representations
 *
 * This class contains Image related TypeConverters:
 *   - ImageSize to String
 *   - Bitmap to ByteArray
 */
class CustomTypeConvertersImage {
    @TypeConverter
    fun fromImageSize(imageSize: ImageSizeInternal): String {
        return when (imageSize) {
            ImageSizeInternal.is256x256 -> "256x256"
            ImageSizeInternal.is512x512 -> "512x512"
            ImageSizeInternal.is1024x1024 -> "1024x1024"
            else -> {
                throw IllegalArgumentException("Unknown ImageSize")
            }
        }
    }

    @TypeConverter
    fun toImageSize(imageSize: String): ImageSizeInternal {
        return when (imageSize) {
            "256x256" -> ImageSizeInternal.is256x256
            "512x512" -> ImageSizeInternal.is512x512
            "1024x1024" -> ImageSizeInternal.is1024x1024
            else -> {
                throw IllegalArgumentException("Unknown ImageSize")
            }
        }
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}