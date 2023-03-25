package com.mohandass.botforge.sync.model

data class Bot(
    val uuid: String,
    val name: String,
    var alias: String,
    val systemMessage: String,
    val createdAt: Long = System.currentTimeMillis(),

    val description: String,
    val tags: String,

    val usersCount: Int,
    val userUpVotes: Int,
    val userDownVotes: Int,

    val updatedAt: Long,
    val createdBy: String,
)
