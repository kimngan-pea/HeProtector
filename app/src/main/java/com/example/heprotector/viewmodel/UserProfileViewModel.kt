package com.example.heprotector.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.model.UserProfile
import com.example.heprotector.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserProfileRepository(
        AppDatabase.getInstance(application).userProfileDao()
    )

    private val _profile = MutableLiveData<UserProfile?>()
    val profile: LiveData<UserProfile?> = _profile

    private var currentUserId: Int? = null

    fun setUserId(userId: Int) {
        currentUserId = userId
        observeProfileByUserId(userId)
    }

    private fun observeProfileByUserId(userId: Int) {
        viewModelScope.launch {
            repository.getProfileByUserId(userId).collectLatest {
                _profile.postValue(it)
            }
        }
    }

    fun saveProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            currentUserId?.let {
                val profileToSave = userProfile.copy(userId = it)
                println(">> ViewModel: Saving profile = $profileToSave")
                repository.insert(profileToSave)
            }
        }
    }

    fun deleteProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            repository.delete(userProfile)
        }
    }

    fun updateGoalWeight(newGoalWeight: Float) {
        viewModelScope.launch {
            _profile.value?.let { currentProfile ->
                val updatedProfile = currentProfile.copy(goalWeight = newGoalWeight)
                repository.update(updatedProfile)
                _profile.postValue(updatedProfile)
            }
        }
    }
}

