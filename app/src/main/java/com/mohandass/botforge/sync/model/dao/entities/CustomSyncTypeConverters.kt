// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.model.dao.entities

import androidx.room.TypeConverter

/**
 * A class to convert custom types to and from the database
 */
class CustomSyncTypeConverters {
    @TypeConverter
    fun convertListToString(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun convertStringToList(string: String): List<String> {
        return string.split(",").toList()
    }
}
