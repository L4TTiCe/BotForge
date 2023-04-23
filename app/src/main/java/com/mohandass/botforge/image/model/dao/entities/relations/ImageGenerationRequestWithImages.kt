package com.mohandass.botforge.image.model.dao.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE

data class ImageGenerationRequestWithImages (
    @Embedded val imageGenerationRequest: ImageGenerationRequestE,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "generationRequestUuid",
        entity = GeneratedImageE::class
    )
    val generatedImages: List<GeneratedImageE>
)
