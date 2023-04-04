package com.mohandass.botforge.chat.services.implementation

import com.mohandass.botforge.chat.model.Chat
import com.mohandass.botforge.chat.model.Message
import com.mohandass.botforge.chat.model.MessageMetadata
import com.mohandass.botforge.chat.model.dao.ChatDao
import com.mohandass.botforge.chat.model.dao.entities.ChatE
import com.mohandass.botforge.chat.model.dao.entities.MessageE
import com.mohandass.botforge.chat.model.dao.entities.MessageMetadataE
import com.mohandass.botforge.common.services.Logger

/**
 * This Class calls the DAO to perform CRUD operations on the Chat table
 */
class ChatServiceImpl(
    private val chatDao: ChatDao,
    private val logger: Logger
) {

    // Save a chat and its messages
    suspend fun saveChat(chat: Chat, messageList: List<Message>) {
        val chatE = ChatE.from(chat)

        val messagesEList = mutableListOf<MessageE>()
        val metadataEList = mutableListOf<MessageMetadataE>()

        // Split the messages into two lists, one for the messages and one for the metadata
        messageList.map { message ->
            val messageE = MessageE.from(message)
            messageE.chatUuid = chatE.uuid

            if (message.metadata != null) {
                val metadataE = MessageMetadataE.from(message.metadata!!)
                messageE.metadataOpenAiId = metadataE!!.openAiId
                metadataEList.add(metadataE)
            } else {
                logger.logVerbose(TAG, "saveChat() message.metadata is null")
                messageE.metadataOpenAiId = null
            }

            messagesEList.add(messageE)
        }

        logger.logVerbose(TAG, "saveChat() Saving chat: $chatE")
        logger.logVerbose(TAG, "saveChat() Saving messageList: $messagesEList")
        logger.logVerbose(TAG, "saveChat() Saving metadataList: $metadataEList")

        // Save the chat, messages and metadata
        chatDao.insertChat(chatE)

        for (metadataE in metadataEList) {
            logger.logVerbose(TAG, "saveChat() Saving metadata: $metadataE")
            chatDao.insertMetadata(metadataE)
        }

        for (messageE in messagesEList) {
            logger.logVerbose(TAG, "saveChat() Saving message: $messageE")
            logger.logVerbose(TAG, "saveChat() Saving message: ${messageE.role}")
            chatDao.insertMessage(messageE)
        }
    }

    // Get the number of messages in a chat
    suspend fun getMessagesCount(chatUUID: String): Int {
        logger.logVerbose(TAG, "getMessagesCount() chatUUID: $chatUUID")
        val count = chatDao.getMessageCount(chatUUID)
        logger.logVerbose(TAG, "getMessagesCount() count: $count")
        return count
    }

    // Get all chats
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

    // Get all messages in a chat, with their metadata, if any, and return them as a list
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