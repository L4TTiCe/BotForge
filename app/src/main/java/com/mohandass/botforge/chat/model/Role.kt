// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatRole
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.mohandass.botforge.R

/**
 * An enum class to represent the Role associated with a message
 *
 * Roles are also used to determine the color of their associated UI elements
 */
class Role(val ordinal: Int) {

    companion object {
        val USER = Role(0)
        val BOT = Role(1)
        val SYSTEM = Role(2)

        private val values = mapOf(
            "USER" to USER,
            "BOT" to BOT,
            "SYSTEM" to SYSTEM,
        )

        fun valueOf(name: String): Role? {
            return values[name]
        }

        fun values(): List<Role> {
            return values.values.toList()
        }

        // Maps OpenAI ChatRole to 'this' Role
        @OptIn(BetaOpenAI::class)
        fun from(charRole: ChatRole): Role {
            return when (charRole) {
                ChatRole.User -> USER
                ChatRole.Assistant -> BOT
                ChatRole.System -> SYSTEM
                else -> {
                    throw IllegalArgumentException("Unknown role: $charRole")
                }
            }
        }
    }

    @OptIn(BetaOpenAI::class)
    fun toChatRole(): ChatRole {
        return when (this) {
            USER -> ChatRole.User
            BOT -> ChatRole.Assistant
            SYSTEM -> ChatRole.System
            else -> {
                throw IllegalArgumentException("Unknown role: $this")
            }
        }
    }

    // Used to determine the color of Message Box at MessageEntry.kt
    @Composable
    fun cardColors(): CardColors {
        return when (this) {
            USER -> CardDefaults.cardColors()
            BOT -> CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )

            SYSTEM -> CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )

            else -> {
                throw IllegalArgumentException("Unknown role: $this")
            }
        }
    }

    @Composable
    fun labelColor(): Color {
        return when (this) {
            USER -> MaterialTheme.colorScheme.primary
            BOT -> MaterialTheme.colorScheme.tertiary
            SYSTEM -> MaterialTheme.colorScheme.secondary
            else -> {
                throw IllegalArgumentException("Unknown role: $this")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun textFieldColors(): TextFieldColors {
        return when (this) {
            USER -> TextFieldDefaults.textFieldColors()
            BOT -> TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                cursorColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary
            )

            SYSTEM -> TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                cursorColor = MaterialTheme.colorScheme.secondary,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary
            )

            else -> {
                throw IllegalArgumentException("Unknown role: $this")
            }
        }
    }

    @Composable
    fun markdownColor(): Color {
        return when (this) {
            USER -> MaterialTheme.colorScheme.onBackground
            BOT -> MaterialTheme.colorScheme.onTertiaryContainer
            SYSTEM -> MaterialTheme.colorScheme.onSecondaryContainer
            else -> {
                throw IllegalArgumentException("Unknown role: $this")
            }
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            USER -> stringResource(id = R.string.user)
            BOT -> stringResource(id = R.string.bot)
            SYSTEM -> stringResource(id = R.string.system)
            else -> {
                throw IllegalArgumentException("Unknown role: $this")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Role && this.ordinal == other.ordinal
    }

    override fun hashCode(): Int {
        return ordinal.hashCode()
    }

    override fun toString(): String {
        return values.entries.find { it.value == this }?.key ?: "Unknown"
    }
}

// A custom type adapter for Role class
class RoleAdapter : TypeAdapter<Role>() {

    override fun write(out: JsonWriter, value: Role) {
        // Write the role name as a string
        out.value(value.toString())
    }

    override fun read(`in`: JsonReader): Role {
        // Read the role name as a string and return the corresponding Role object
        val name = `in`.nextString()
        return Role.valueOf(name) ?: Role.USER // default to USER if unknown
    }
}
