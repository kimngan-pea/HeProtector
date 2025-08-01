package com.example.heprotector.data.dao

import androidx.room.*
import com.example.heprotector.data.model.UserAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserAccount): Long

    @Query("SELECT * FROM user_accounts WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE id = :id")
    suspend fun getUserById(id: Int): UserAccount?

    @Update
    suspend fun update(user: UserAccount)
}
