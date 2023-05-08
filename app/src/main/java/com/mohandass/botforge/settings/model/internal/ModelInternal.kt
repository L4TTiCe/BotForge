// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.settings.model.internal

import com.aallam.openai.api.model.Model

data class ModelInternal (
    val model: Model
) {
    override fun toString(): String {
        return model.id.id
    }

    fun toModel(): Model {
        return model
    }
}
