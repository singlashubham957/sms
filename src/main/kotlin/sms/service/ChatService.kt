package sms.service

import sms.data.ChatData
import sms.data.UserData
import sms.models.Chat
import sms.models.Message

class ChatService(
    private val userData: UserData,
    private val messageService: MessageService,
    private val chatData: ChatData
) {
    fun sendMessage(senderId: String, receiverId: String, text: String) {
        var chatId = getChatId(senderId, receiverId)
        if (chatId == null) {
            chatId = addChat(senderId, receiverId)
        }
        messageService.addMessage(senderId, chatId, text)
    }

    fun addChat(userId1: String, userId2: String): String {
        val existingChatId = chatData.getChatId(userId1, userId2)
        if (existingChatId != null) {
            throw Exception("Chat already exists!")
        }

        return chatData.addChat(userId1, userId2)
    }

    fun getAllChatsOfUser(userId: String): List<Chat> {
        return chatData.getAllChatsOfUser(userId)
    }

    fun getChatId(userId1: String, userId2: String): String? {
        return chatData.getChatId(userId1, userId2)
    }

    fun getAllUnreadMessages(userId: String): List<Message> {
        val chatIds = chatData.getAllChatsOfUser(userId).map { it.id }
        return messageService.getAllUnreadMessages(chatIds, userId)
    }

    fun getAllMessagesWithUser(userId: String, userId2: String): List<Message> {
        val chatId = chatData.getChatId(userId, userId2)!!
        return messageService.getAllMessagesOfChat(chatId)
    }
}
