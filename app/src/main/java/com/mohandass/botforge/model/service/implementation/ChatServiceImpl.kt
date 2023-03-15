package com.mohandass.botforge.model.service.implementation

import android.util.Log
import com.mohandass.botforge.model.Chat
import com.mohandass.botforge.model.dao.ChatDao
import com.mohandass.botforge.model.entities.ChatE
import com.mohandass.botforge.model.entities.MessageE
import com.mohandass.botforge.model.entities.MessageMetadataE

class ChatServiceImpl(private val chatDao: ChatDao) {

    suspend fun saveChat(char: Chat) {
        val chatE = ChatE.from(char)

        val messagesEList = mutableListOf<MessageE>()
        val metadataEList = mutableListOf<MessageMetadataE>()
        char.messages.map { message ->
            val messageE = MessageE.from(message)
            messageE.chatUuid = chatE.uuid

            messagesEList.add(messageE)

            if (message.metadata != null) {
                val metadataE = MessageMetadataE.from(message.metadata)
                metadataEList.add(metadataE!!)
            }
        }

        Log.v(TAG, "Saving chat: $chatE")
        Log.v(TAG, "Saving messageList: $messagesEList")
        Log.v(TAG, "Saving metadataList: $metadataEList")

        chatDao.insertChat(chatE)

        for (messageE in messagesEList) {
            Log.v(TAG, "Saving message: $messageE")
            Log.v(TAG, "Saving message: ${messageE.role.toString()}")
            chatDao.insertMessage(messageE)
        }

        for (metadataE in metadataEList) {
            Log.v(TAG, "Saving metadata: $metadataE")
            chatDao.insertMetadata(metadataE)
        }
    }

    companion object {
        private const val TAG = "ChatServiceImpl"
    }
}