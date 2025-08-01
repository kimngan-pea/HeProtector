package com.example.heprotector.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector.data.dao.BloodSugarDao
import com.example.heprotector.data.model.BloodSugar
import com.example.heprotector.data.model.HbA1c
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BloodSugarViewModel(private val dao: BloodSugarDao) : ViewModel() {

    private val _list = MutableLiveData<List<BloodSugar>>()
    val list: LiveData<List<BloodSugar>> = _list

    fun addEntry(entry: BloodSugar) {
        viewModelScope.launch {
            dao.insert(entry)
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
