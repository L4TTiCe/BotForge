// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.model

import com.aallam.openai.api.model.Model
import com.mohandass.botforge.settings.model.internal.ModelInternal

// Converts ImageSize to ImageSizeInternal
fun Model.toInternal(): ModelInternal {
    return ModelInternal(
        this
    )
}


