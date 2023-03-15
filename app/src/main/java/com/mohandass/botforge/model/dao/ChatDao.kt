package com.mohandass.botforge.model.dao

import androidx.room.*
import com.mohandass.botforge.model.entities.ChatE
import com.mohandass.botforge.model.entities.MessageE
import com.mohandass.botforge.model.entities.MessageMetadataE
import com.mohandass.botforge.model.entities.relations.ChatWithMessages
import com.mohandass.botforge.model.entities.relations.MessageAndMetadata

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageE)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: MessageMetadataE)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatE)

    @Query("SELECT * FROM chats ORDER BY savedAt DESC")
    suspend fun getAllChats(): List<ChatE>

    @Transaction
    @Query("SELECT * FROM messages WHERE uuid = :messageUUID")
    suspend fun getMessageWithMetadata(messageUUID: String): List<MessageAndMetadata>

    @Transaction
    @Query("SELECT * FROM chats WHERE uuid = :chatUUID")
    suspend fun getChatWithMessages(chatUUID: String): List<ChatWithMessages>
}
