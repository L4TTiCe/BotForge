package com.mohandass.botforge.image.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE
import com.mohandass.botforge.image.model.dao.entities.relations.ImageGenerationRequestWithImages

@Dao
interface ImageGenerationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImageGenerationRequest(imageGenerationRequest: ImageGenerationRequestE)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGeneratedImage(generatedImage: GeneratedImageE)

    @Transaction
    @Query("SELECT * FROM imageGenerationRequests WHERE uuid = :generationRequestUuid")
    suspend fun getGeneratedImagesFromImageGenerationRequestId(generationRequestUuid: String): List<ImageGenerationRequestWithImages>

    @Transaction
    @Query("SELECT * FROM imageGenerationRequests ORDER BY imageGenerationRequests.timestamp DESC")
    suspend fun getAllGeneratedImages(): List<ImageGenerationRequestWithImages>

    @Query("DELETE FROM imageGenerationRequests WHERE uuid = :generationRequestUuid")
    suspend fun deleteImageGenerationRequestById(generationRequestUuid: String)

    @Query("DELETE FROM generatedImages WHERE uuid = :generatedImageUuid")
    suspend fun deleteGeneratedImageById(generatedImageUuid: String)

    @Query("DELETE FROM imageGenerationRequests")
    suspend fun deleteAllImageGenerationRequests()
}
