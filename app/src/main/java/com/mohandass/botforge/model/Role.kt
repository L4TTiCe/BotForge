package com.mohandass.botforge.model

enum class Role {
    USER,
    BOT;

    fun isUser() = this == USER
}
