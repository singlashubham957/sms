package sms.data

import sms.models.Chat
import java.util.UUID

class ChatData {
    private val chats: MutableList<Chat> = mutableListOf()

    fun getChatId(userId1: String, userId2: String): String? {
        val chat = chats.find {
            (it.userId1 == userId1 && it.userId2 == userId2) || (it.userId1 == userId2 && it.userId2 == userId1)
        }
        return chat?.id
    }

    fun getChat(id: String): Chat? {
        return chats.find { it.id == id }
    }

    fun getAllChatsOfUser(userId: String): List<Chat> {
        return chats.filter { it.userId1 == userId || it.userId2 == userId }
    }

    fun addChat(userId1: String, userId2: String): String {
        val chat = Chat(
            id = UUID.randomUUID().toString(),
            userId1 = userId1,
            userId2 = userId2
        )

        chats.add(chat)
        return chat.id
    }
}
