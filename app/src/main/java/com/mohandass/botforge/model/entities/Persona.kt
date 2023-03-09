package com.mohandass.botforge.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Persona(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val systemMessage: String = "",

    val createdAt: Long = System.currentTimeMillis(),
    val lastUsed: Long = System.currentTimeMillis(),
) {
    override fun toString(): String {
        return "Persona(uuid='$uuid', name='$name', systemMessage='$systemMessage', createdAt=$createdAt, lastUsed=$lastUsed)"
    }
}
