package com.mohandass.botforge.image.model.dao.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.UUID

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
