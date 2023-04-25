// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.image.model

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.image.ImageSize

@OptIn(BetaOpenAI::class)
fun ImageSize.toInternal(): ImageSizeInternal {
    return when (this) {
        ImageSize.is256x256 -> ImageSizeInternal.is256x256
        ImageSize.is512x512 -> ImageSizeInternal.is512x512
        ImageSize.is1024x1024 -> ImageSizeInternal.is1024x1024
        else -> {
            throw IllegalArgumentException("Unknown image size: $this")
        }
    }
}


