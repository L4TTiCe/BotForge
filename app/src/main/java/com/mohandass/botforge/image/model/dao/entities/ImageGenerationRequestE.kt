package com.mohandass.botforge.image.model.dao.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mohandass.botforge.image.model.ImageSizeInternal
import java.sql.Timestamp
import java.util.UUID

@Entity(tableName = "imageGenerationRequests")
@TypeConverters(CustomTypeConvertersImage::class)
data class ImageGenerationRequestE(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val prompt: String = "",
    val n: Int = 1,
    val imageSize: ImageSizeInternal? = null,
    val timestamp: Long = Timestamp(System.currentTimeMillis()).time,
)
