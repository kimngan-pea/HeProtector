package com.example.heprotector.data.dao

import androidx.room.*
import com.example.heprotector.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile)

    @Update
    suspend fun update(userProfile: UserProfile)

    @Delete
    suspend fun delete(userProfile: UserProfile)

    // 🔹 Lấy profile theo userId (cho login/session)
    @Query("SELECT * FROM user_profile WHERE userId = :userId LIMIT 1")
    fun getProfileByUserId(userId: Int): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE userId = :userId LIMIT 1")
    suspend fun getProfileOnceByUserId(userId: Int): UserProfile?

    // 🔸 Tùy chọn: vẫn giữ bản cũ nếu bạn chỉ lưu 1 profile duy nhất
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getProfileOnce(): UserProfile?
}
