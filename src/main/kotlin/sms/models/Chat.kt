package sms.models

import java.time.LocalDateTime

class Chat(
    val id: String,
    val userId1: String,
    val userId2: String
)

class Message(
    val id: String,
    val text: String,
    val chatId: String,
    val senderId: String,
    var seen: Boolean,
    var delivered: Boolean,
    val createdAt: LocalDateTime
)
