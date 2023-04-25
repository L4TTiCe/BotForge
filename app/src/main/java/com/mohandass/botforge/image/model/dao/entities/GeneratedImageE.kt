// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.image.model.dao.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.UUID

/**
 * A data class to represent a GeneratedImage
 *
 * This entity is used to store the GeneratedImage.
 * Contains a foreign key to the ImageGenerationRequest
 */
@Entity(
    tableName = "generatedImages",
    foreignKeys = [
        ForeignKey(
            entity = ImageGenerationRequestE::class,
            parentColumns = ["uuid"],
            childColumns = ["generationRequestUuid"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("generationRequestUuid")]
)
@TypeConverters(CustomTypeConvertersImage::class)
data class GeneratedImageE constructor(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val bitmap: Bitmap? = null,
    val generationRequestUuid: String = "",
)
