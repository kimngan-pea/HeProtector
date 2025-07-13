package com.example.heprotector_.data.repository

import com.example.heprotector_.data.db.dao.UserDao
import com.example.heprotector_.data.db.entities.User_Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(username: String, email: String, passwordHash: String, height: Double?): Long =
        withContext(Dispatchers.IO) {
            val newUser = User_Account(username = username, email = email, passwordHash = passwordHash, height = height)
            userDao.insertUser(newUser)
        }

    suspend fun loginUser(username: String, passwordHash: String): User_Account? =
        withContext(Dispatchers.IO) {
            val user = userDao.getUserByUsername(username)
            // In a real app, verify hashed password here, not just direct comparison
            if (user != null && user.passwordHash == passwordHash) {
                user
            } else {
                null
            }
        }

    suspend fun getUserById(userId: Int): User_Account? =
        withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }

    suspend fun updateHeight(userId: Int, height: Double) =
        withContext(Dispatchers.IO) {
            userDao.updateHeight(userId, height)
        }
}