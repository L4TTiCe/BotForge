// SPDX-FileCopyrightText: 2023 Dheshan Mohandass (L4TTiCe)
//
// SPDX-License-Identifier: MIT

package com.mohandass.botforge.chat.model.dao

import androidx.room.*
import com.mohandass.botforge.chat.model.dao.entities.ChatE
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import com.mohandass.botforge.chat.model.dao.entities.MessageMetadataE
import com.mohandass.botforge.chat.model.dao.entities.relations.ChatWithMessages
import com.mohandass.botforge.chat.model.dao.entities.relations.MessageAndMetadata

/**
 * A DAO to interact with the [ChatE], [MessageE] and [MessageMetadataE] entities
 */
@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageE)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMetadata(metadata: MessageMetadataE)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatE)

    @Query("SELECT COUNT(*) FROM messages WHERE chatUuid = :chatUUID")
    suspend fun getMessageCount(chatUUID: String): Int

    @Query("SELECT * FROM chats ORDER BY savedAt DESC")
    suspend fun getAllChats(): List<ChatE>

    @Transaction
    @Query("SELECT * FROM messages WHERE uuid = :messageUUID")
    suspend fun getMessageWithMetadata(messageUUID: String): List<MessageAndMetadata>

    @Transaction
    @Query("SELECT * FROM chats WHERE uuid = :chatUUID")
    suspend fun getChatWithMessages(chatUUID: String): List<ChatWithMessages>

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()

    @Query("DELETE FROM chats WHERE uuid = :chatUUID")
    suspend fun deleteChatByUUID(chatUUID: String)
}
