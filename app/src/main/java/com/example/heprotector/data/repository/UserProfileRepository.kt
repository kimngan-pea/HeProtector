package com.example.heprotector.data.repository

import com.example.heprotector.data.dao.UserProfileDao
import com.example.heprotector.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(private val dao: UserProfileDao) {

    fun getProfile(): Flow<UserProfile?> {
        return dao.getProfile()
    }

    // add function to take profile follow userId (Flow)
    fun getProfileByUserId(userId: Int): Flow<UserProfile?> {
        return dao.getProfileByUserId(userId)
    }

    // add function to take profile follow userId (once)
    suspend fun getProfileOnceByUserId(userId: Int): UserProfile? {
        return dao.getProfileOnceByUserId(userId)
    }

    suspend fun insert(profile: UserProfile) {
        dao.insert(profile)
    }

    suspend fun update(profile: UserProfile) {
        dao.update(profile)
    }

    suspend fun delete(profile: UserProfile) {
        dao.delete(profile)
    }

    suspend fun getProfileOnce(): UserProfile? {
        return dao.getProfileOnce()
    }
}
