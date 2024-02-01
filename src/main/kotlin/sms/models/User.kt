package sms.models

data class User(
    val id: String,
    val password: String,
    var isLoggedIn: Boolean = false
)

data class CreateUserResponse(
    val status: String,
    val message: String
)

data class SendMessageBody(
    val text: String
)
