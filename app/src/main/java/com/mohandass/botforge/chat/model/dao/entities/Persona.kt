package com.mohandass.botforge.chat.model.dao.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Persona(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    @ColumnInfo(defaultValue = "") val parentUuid: String = "",
    val name: String = "",
    var alias: String = "",
    val systemMessage: String = "",

    val createdAt: Long = System.currentTimeMillis(),
    val lastUsed: Long = System.currentTimeMillis(),
) {
    override fun toString(): String {
        return "Persona(" +
                "uuid='$uuid'," +
                "parentUuid='$parentUuid', " +
                "alias='$alias', " +
                "name='$name', " +
                "systemMessage='$systemMessage', " +
                "createdAt=$createdAt, " +
                "lastUsed=$lastUsed" +
                ")"
    }
}
