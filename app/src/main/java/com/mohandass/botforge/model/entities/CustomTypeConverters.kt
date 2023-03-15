package com.mohandass.botforge.model.entities

import androidx.room.TypeConverter
import com.mohandass.botforge.model.Role

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