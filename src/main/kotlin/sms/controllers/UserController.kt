package sms.controllers

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import sms.models.CreateUserResponse
import sms.models.Message
import sms.models.SendMessageBody
import sms.models.User
import sms.service.ChatService
import sms.service.MessageService
import sms.service.UserService

@Path("/")
class UserController {
    @Inject
    private lateinit var chatService: ChatService

    @Inject
    private lateinit var userService: UserService

    @Inject
    private lateinit var messageService: MessageService

    @GET
    @Path("/sms/v1/health")
    @Produces("application/json")
    fun getHealth(): String {
        return "Hey! How are you??"
    }

    @POST
    @Path("/sms/v1/user")
    @Produces("application/json")
    fun createUser(user: User): CreateUserResponse {
        try {
            userService.addUser(user)
        } catch (e: Exception) {
            if (e.message == "user already exists") {
                return CreateUserResponse("failure", "user already exists")
            }
            throw e
        }
        return CreateUserResponse("success", "user created successfully")
    }

    @POST
    @Path("/sms/v1/user/login")
    @Produces("application/json")
    fun login(user: User): String {
        val valid = userService.login(user)
        return if (valid) {
            "success"
        } else {
            "failure"
        }
    }

    @POST
    @Path("/sms/v1/user/logout")
    @Produces("application/json")
    fun logout(user: User): String {
        userService.logout(user)
        return "success"
    }

    @GET
    @Path("/sms/v1/users/all")
    @Produces("application/json")
    fun fetchAllUsers(): List<String> {
        return userService.getAllUsers()
    }

    @GET
    @Path("/sms/v1/user/fetch/unread-messages")
    @Produces("application/json")
    fun fetchUnreadMessages(@HeaderParam("userId")userId: String): List<Message> {
        return chatService.getAllUnreadMessages(userId)
    }

    @POST
    @Path("/sms/v1/user/message/{toUserId}")
    @Produces("application/json")
    fun sendMessage(@HeaderParam("userId")userId: String, @PathParam("toUserId")toUserId: String, sendMessageBody: SendMessageBody): String {
        chatService.sendMessage(userId, toUserId, sendMessageBody.text)
        return "success"
    }

    @GET
    @Path("/sms/v1/user/chat/{withUserId}")
    @Produces("application/json")
    fun fetchMessagesOfChatWithUser(@HeaderParam("userId")userId: String, @PathParam("withUserId")withUserId: String): List<Message> {
        return chatService.getAllMessagesWithUser(userId, withUserId)
    }

    @PUT
    @Path("/sms/v1/user/chat/message/{id}/mark-read")
    @Produces("application/json")
    fun markMessageRead(@PathParam("id")messageId: String) {
        return messageService.markMessageRead(messageId)
    }
}
