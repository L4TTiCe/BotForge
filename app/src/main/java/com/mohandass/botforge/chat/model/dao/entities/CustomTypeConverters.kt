// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model.dao.entities

import androidx.room.TypeConverter
import com.mohandass.botforge.chat.model.Role

/**
 * A class to convert custom types to and from the Database
 */
class CustomTypeConverters {
    @TypeConverter
    fun fromRole(role: Role): Int {
        return role.ordinal
    }

    @TypeConverter
    fun toRole(role: Int): Role {
        return Role.values()[role]
    }
}