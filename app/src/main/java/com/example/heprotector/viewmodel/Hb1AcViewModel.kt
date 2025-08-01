package com.example.heprotector.viewmodel

import androidx.lifecycle.*
import com.example.heprotector.data.dao.HbA1cDao
import com.example.heprotector.data.model.HbA1c
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HbA1cViewModel(private val dao: HbA1cDao) : ViewModel() {

    private val _list = MutableLiveData<List<HbA1c>>()
    val list: LiveData<List<HbA1c>> = _list

    fun insertHbA1c(value: Float, userId: Int) {
        val newHbA1c = HbA1c(
            value = value,
            time = System.currentTimeMillis(),
            userId = userId
        )
        viewModelScope.launch {
            dao.insert(newHbA1c)
        }
    }

    fun observeDataByUser(userId: Int) {
        viewModelScope.launch {
            dao.getByUserId(userId).collectLatest {
                _list.postValue(it)
            }
        }
    }
}
