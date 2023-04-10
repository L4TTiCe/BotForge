// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model.dao.entities

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohandass.botforge.common.Samples
import java.util.*

/**
 * A data class to represent a Persona
 *
 * This class is used to represent a Persona saved in the Database
 */
@Entity
data class Persona(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),

    // This is the UUID of the Persona, this persona was created from.
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

class PersonaProvider: PreviewParameterProvider<Persona> {
    private val shortMessage = Persona(
                uuid = UUID.randomUUID().toString(),
                parentUuid = UUID.randomUUID().toString(),
                name = Samples.name,
                alias = Samples.emoji,
                systemMessage = Samples.systemMessage2,
                createdAt = System.currentTimeMillis(),
                lastUsed = System.currentTimeMillis(),
            )

    private val community = Persona(
                uuid = UUID.randomUUID().toString(),
                parentUuid = UUID.randomUUID().toString(),
                name = Samples.name,
                alias = Samples.emoji,
                systemMessage = Samples.systemMessage,
                createdAt = System.currentTimeMillis(),
                lastUsed = System.currentTimeMillis(),
            )

    private val default = Persona(
                uuid = UUID.randomUUID().toString(),
                parentUuid = "",
                name = Samples.name,
                alias = Samples.emoji,
                systemMessage = Samples.systemMessage,
                createdAt = System.currentTimeMillis(),
                lastUsed = System.currentTimeMillis(),
            )

    private val longName = Persona(
                uuid = UUID.randomUUID().toString(),
                parentUuid = UUID.randomUUID().toString(),
                name = Samples.name + Samples.name + Samples.name + Samples.name + Samples.name,
                alias = Samples.emoji,
                systemMessage = Samples.systemMessage,
                createdAt = System.currentTimeMillis(),
                lastUsed = System.currentTimeMillis(),
            )

    override val values: Sequence<Persona> = sequenceOf(shortMessage, community, default, longName)
}
