package com.mohandass.botforge.model

import java.util.*

class Persona {
    private val uuid = UUID.randomUUID().toString()
    private var name: String = ""
    private var systemMessage: String = ""

    fun getUuid(): String {
        return uuid
    }
    fun getName(): String {
        return name
    }
    fun getSystemMessage(): String {
        return systemMessage
    }

    fun setName(name: String) {
        this.name = name
    }
    fun setSystemMessage(systemMessage: String) {
        this.systemMessage = systemMessage
    }

    override fun toString(): String {
        return "Persona(uuid=$uuid, name=$name, systemMessage=$systemMessage)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Persona) return false

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (systemMessage != other.systemMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (systemMessage?.hashCode() ?: 0)
        return result
    }
}
