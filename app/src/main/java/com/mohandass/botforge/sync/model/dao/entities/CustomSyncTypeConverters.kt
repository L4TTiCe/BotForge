package com.mohandass.botforge.sync.model.dao.entities

import androidx.room.TypeConverter

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