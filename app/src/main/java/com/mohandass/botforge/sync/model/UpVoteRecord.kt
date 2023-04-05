// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.sync.model

/*
 * A data class to represent a VoteRecord
 *
 * Used to represent a UpVote or DownVote or Reports in the Firestore Database
 */
data class VoteRecord(
    val botId: String,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)
