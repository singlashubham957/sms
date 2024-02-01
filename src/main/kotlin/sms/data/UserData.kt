package sms.data

import sms.models.User

class UserData {
    private val userMap: MutableMap<String, User> = mutableMapOf()

    fun getUser(id: String): User? {
        return userMap[id]
    }

    fun getAllUsers(): List<String> {
        return userMap.values.map { it.id }
    }

    fun addUser(user: User) {
        userMap[user.id] = user
    }

    fun markUserLoggedIn(userId: String) {
        userMap[userId]!!.isLoggedIn = true
    }

    fun markUserLoggedOut(userId: String) {
        userMap[userId]!!.isLoggedIn = false
    }
}
