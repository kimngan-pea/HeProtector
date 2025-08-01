// UserPreferences.kt
package com.example.heprotector.data.pref

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.heprotector.data.dao.UserProfileDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(
    private val context: Context,
    private val profileDao: UserProfileDao
) {
    companion object {
        val USER_ID = intPreferencesKey("user_id")
    }

    val userIdFlow: Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[USER_ID]
    }

    suspend fun saveUserId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { it.clear() }
    }
}
