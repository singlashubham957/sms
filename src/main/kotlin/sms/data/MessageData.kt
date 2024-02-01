package sms.data

import sms.models.Message
import java.time.LocalDateTime
import java.util.UUID

class MessageData {
    private val messages: MutableList<Message> = mutableListOf()

    fun addMessage(senderId: String, text: String, chatId: String) {
        synchronized(
            "chat"
        ) {
            messages.add(
                Message(
                    id = UUID.randomUUID().toString(),
                    chatId = chatId,
                    senderId = senderId,
                    seen = false,
                    delivered = false,
                    createdAt = LocalDateTime.now(),
                    text = text
                )
            )
        }
    }

    fun getMessageById(id: String): Message? {
        return messages.find { it.id == id }
    }

    fun markMessageRead(messageId: String) {
        messages.filter { it.id == messageId }[0].seen = true
    }

    fun markMessageDelivered(messageId: String) {
        messages.filter { it.id == messageId }[0].delivered = true
    }

    fun getMessagesOfChat(chatId: String): List<Message> {
        return messages.filter { it.chatId == chatId }.sortedBy { it.createdAt }
    }

    fun getUnReadMessagesOfChats(chatIds: List<String>, userId: String): List<Message> {
        return messages.filter {
            chatIds.contains(it.chatId) &&
                !it.seen &&
                it.senderId != userId
        }.sortedBy { it.createdAt }
    }

    fun getUnDeliveredMessagesOfChat(chatId: String): List<Message> {
        return getMessagesOfChat(chatId).filter { !it.delivered }.sortedBy { it.createdAt }
    }
}
