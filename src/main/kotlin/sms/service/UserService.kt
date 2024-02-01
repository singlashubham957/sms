package sms.service

import sms.data.UserData
import sms.models.User

class UserService(
    private val userData: UserData
) {
    fun addUser(user: User) {
        if (userData.getUser(user.id) != null) {
            throw Exception("user already exists")
        }
        userData.addUser(user)
    }

    fun login(user: User): Boolean {
        val userCreds = userData.getUser(user.id)!!
        if (userCreds.password == user.password) {
            userData.markUserLoggedIn(user.id)
            return true
        }
        return false
    }

    fun logout(user: User) {
        userData.markUserLoggedOut(user.id)
    }

    fun getAllUsers(): List<String> {
        return userData.getAllUsers()
    }

    fun getUser(user: User): User? {
        return userData.getUser(user.id)
    }
}

/*
{


}
 */
