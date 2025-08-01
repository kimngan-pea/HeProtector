package com.example.heprotector.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.heprotector.data.AppDatabase
import com.example.heprotector.data.model.Weight
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WeightViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).weightDao()

    private val _list = MutableLiveData<List<Weight>>()
    val list: LiveData<List<Weight>> = _list

    private var currentUserId: Int? = null

    init {
        viewModelScope.launch {
            dao.getAll().collectLatest {
                _list.postValue(it)
            }
        }
    }

    fun loadWeights(userId: Int) {
        currentUserId = userId
        viewModelScope.launch {
            dao.getWeightsByUser(userId).collectLatest {
                _list.postValue(it)
            }
        }
    }
    fun addEntry(entry: Weight) {
        viewModelScope.launch {
            dao.insert(entry)
        }
    }

    fun deleteEntry(entry: Weight) {
        viewModelScope.launch {
            dao.delete(entry)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }
}
