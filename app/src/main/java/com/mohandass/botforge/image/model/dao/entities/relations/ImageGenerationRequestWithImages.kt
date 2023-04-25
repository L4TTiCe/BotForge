// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.image.model.dao.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mohandass.botforge.image.model.dao.entities.GeneratedImageE
import com.mohandass.botforge.image.model.dao.entities.ImageGenerationRequestE

/**
 * A data class to represent a ImageGenerationRequest with its GeneratedImages
 *
 * This relation is used to get a reference to the ImageGenerationRequest and its GeneratedImages
 */
data class ImageGenerationRequestWithImages(
    @Embedded val imageGenerationRequest: ImageGenerationRequestE,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "generationRequestUuid",
        entity = GeneratedImageE::class
    )
    val generatedImages: List<GeneratedImageE>
)
