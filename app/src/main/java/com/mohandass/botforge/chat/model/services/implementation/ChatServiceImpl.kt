package com.mohandass.botforge.chat.model.services.implementation

import android.util.Log
import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.MessageMetadata
import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.entities.ChatE
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import com.mohandass.botforge.chat.model.dao.entities.MessageMetadataE

class ChatServiceImpl(private val chatDao: ChatDao) {

    suspend fun saveChat(chat: Chat, messageList: List<Message>) {
        val chatE = ChatE.from(chat)

        val messagesEList = mutableListOf<MessageE>()
        val metadataEList = mutableListOf<MessageMetadataE>()
        messageList.map { message ->
            val messageE = MessageE.from(message)
            messageE.chatUuid = chatE.uuid

            if (message.metadata != null) {
                val metadataE = MessageMetadataE.from(message.metadata!!)
                messageE.metadataOpenAiId = metadataE!!.openAiId
                metadataEList.add(metadataE)
            } else {
                Log.v(TAG, "saveChat() message.metadata is null")
                messageE.metadataOpenAiId = null
            }

            messagesEList.add(messageE)
        }

        Log.v(TAG, "saveChat() Saving chat: $chatE")
        Log.v(TAG, "saveChat() Saving messageList: $messagesEList")
        Log.v(TAG, "saveChat() Saving metadataList: $metadataEList")

        chatDao.insertChat(chatE)

        for (metadataE in metadataEList) {
            Log.v(TAG, "saveChat() Saving metadata: $metadataE")
            chatDao.insertMetadata(metadataE)
        }

        for (messageE in messagesEList) {
            Log.v(TAG, "saveChat() Saving message: $messageE")
            Log.v(TAG, "saveChat() Saving message: ${messageE.role}")
            chatDao.insertMessage(messageE)
        }
    }

    suspend fun getMessagesCount(chatUUID: String): Int {
        Log.v(TAG, "getMessagesCount() chatUUID: $chatUUID")
        val count = chatDao.getMessageCount(chatUUID)
        Log.v(TAG, "getMessagesCount() count: $count")
        return count
    }

    suspend fun getChats(): List<Chat> {
        val chatEList = chatDao.getAllChats()
        val chatList = mutableListOf<Chat>()

        for (chatE in chatEList) {
            val chat = Chat(
                uuid = chatE.uuid,
                name = chatE.name,
                personaUuid = chatE.personaUuid,
                savedAt = chatE.savedAt,
            )
            chatList += chat
        }
        return chatList
    }

    suspend fun getMessagesFromChat(chatUUID: String): List<Message> {
        val chatWithMessagesEList = chatDao.getChatWithMessages(chatUUID)
        val messageList = mutableListOf<Message>()

        for (chatWithMessagesE in chatWithMessagesEList) {
            val messagesE = chatWithMessagesE.messages

            for (messageE in messagesE) {
                val message = Message.from(messageE)

                if (messageE.metadataOpenAiId != null) {
                    val messageWithMetadataEList = chatDao.getMessageWithMetadata(messageE.uuid)
                    for (messageWithMetadataE in messageWithMetadataEList) {
                        val metadataE = messageWithMetadataE.metadata
                        message.metadata = MessageMetadata.from(metadataE)
                    }
                }

                messageList += message
            }
        }

        return messageList
    }

    suspend fun deleteAllChats() {
        chatDao.deleteAllChats()
    }

    suspend fun deleteChatByUUID(chatUUID: String) {
        chatDao.deleteChatByUUID(chatUUID)
    }

    companion object {
        private const val TAG = "ChatServiceImpl"
    }
}