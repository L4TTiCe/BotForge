package com.mohandass.botforge.auth.model

/**
 * A data class to represent a User
 *
 * This class is used to represent an Authenticated User
 */
data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val displayName: String? = null
)
