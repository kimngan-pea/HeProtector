package com.example.heprotector_.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heprotector_.data.db.entities.User_Account

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User_Account): Long // Returns new row ID

    @Query("SELECT * FROM user_accounts WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User_Account?

    @Query("SELECT * FROM user_accounts WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User_Account?

    @Query("UPDATE user_accounts SET height = :height WHERE userId = :userId")
    suspend fun updateHeight(userId: Int, height: Double)
}