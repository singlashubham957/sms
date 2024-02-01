package sms.service

import sms.data.MessageData
import sms.models.Message

class MessageService(
    private val messageData: MessageData
) {
    fun addMessage(senderId: String, chatId: String, text: String) {
        messageData.addMessage(senderId, text, chatId)
    }

    fun getAllMessagesOfChat(chatId: String): List<Message> {
        return messageData.getMessagesOfChat(chatId)
    }

    fun markMessageRead(messageId: String) {
        val message = messageData.getMessageById(messageId)
        if (message == null) {
            throw Exception("No message exists for id: $messageId")
        }
        messageData.markMessageRead(messageId)
    }

    fun markMessageDelivered(messageId: String) {
        val message = messageData.getMessageById(messageId)
        if (message == null) {
            throw Exception("No message exists for id: $messageId")
        }
        messageData.markMessageDelivered(messageId)
    }

    fun getAllUnreadMessages(chatIds: List<String>, userId: String): List<Message> {
        return messageData.getUnReadMessagesOfChats(chatIds, userId)
    }

    fun getUnreadMessages(chatId: String, userId: String): List<Message> {
        return messageData.getUnReadMessagesOfChats(listOf(chatId), userId)
    }
}
