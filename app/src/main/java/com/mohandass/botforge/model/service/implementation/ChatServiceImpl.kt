package com.mohandass.botforge.model.service.implementation

import android.util.Log
import com.mohandass.botforge.model.Chat
import com.mohandass.botforge.model.Message
import com.mohandass.botforge.model.dao.ChatDao
import com.mohandass.botforge.model.entities.ChatE
import com.mohandass.botforge.model.entities.MessageE
import com.mohandass.botforge.model.entities.MessageMetadataE

class ChatServiceImpl(private val chatDao: ChatDao) {

    suspend fun saveChat(chat: Chat, messageList: List<Message>) {
        val chatE = ChatE.from(chat)

        val messagesEList = mutableListOf<MessageE>()
        val metadataEList = mutableListOf<MessageMetadataE>()
        messageList.map { message ->
            val messageE = MessageE.from(message)
            messageE.chatUuid = chatE.uuid

            messagesEList.add(messageE)

            if (message.metadata != null) {
                val metadataE = MessageMetadataE.from(message.metadata!!)
                metadataEList.add(metadataE!!)
            }
        }

        Log.v(TAG, "saveChat() Saving chat: $chatE")
        Log.v(TAG, "saveChat() Saving messageList: $messagesEList")
        Log.v(TAG, "saveChat() Saving metadataList: $metadataEList")

        chatDao.insertChat(chatE)

        for (messageE in messagesEList) {
            Log.v(TAG, "saveChat() Saving message: $messageE")
            Log.v(TAG, "saveChat() Saving message: ${messageE.role}")
            chatDao.insertMessage(messageE)
        }

        for (metadataE in metadataEList) {
            Log.v(TAG, "saveChat() Saving metadata: $metadataE")
            chatDao.insertMetadata(metadataE)
        }
    }

    suspend fun getMessagesCount(chatUUID: String): Int {
        Log.v(TAG, "getMessagesCount() chatUUID: $chatUUID")
        val count =  chatDao.getMessageCount(chatUUID)
        Log.v(TAG, "getMessagesCount() count: $count")
        return count
    }

    suspend fun getChats(): List<Chat> {
        val chatEList = chatDao.getAllChats()
        val chatList = mutableListOf<Chat>()

        for (chatE in chatEList) {
            val chat = Chat(
                uuid = chatE.uuid,
                name= chatE.name,
                personaUuid = chatE.personaUuid,
                savedAt = chatE.savedAt,
            )
            chatList += chat
        }
        return chatList
    }

    companion object {
        private const val TAG = "ChatServiceImpl"
    }
}