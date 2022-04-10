package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CurrentConditionViewModel @Inject constructor(private val service: Api) : ViewModel() {
    private val _currentCondition = MutableLiveData<CurrentCondition>()
    val currentCondition: LiveData<CurrentCondition> get() = _currentCondition

    fun loadData(zipCode: String) = runBlocking {
        launch {
            _currentCondition.value = service.getCurrentConditions(zipCode)
        }
    }
}