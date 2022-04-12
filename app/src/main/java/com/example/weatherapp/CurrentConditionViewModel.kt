package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class CurrentConditionViewModel @Inject constructor() : ViewModel() {
    private lateinit var currentConditions: CurrentCondition
    private val _currentCondition = MutableLiveData<Coordinates?>()
    val navigateToForecast: LiveData<Coordinates?> = _currentCondition

    private val _viewState = MutableLiveData(State.DEFAULT)
    val viewState: LiveData<State> = _viewState

    fun loadData(currentConditions: CurrentCondition){
        this.currentConditions = currentConditions
        _currentCondition.value = null
        _viewState.value = _viewState.value?.copy(currentConditions = currentConditions)
    }

    fun loadData(){
        _currentCondition.value = currentConditions.coordinates
    }

    data class State(
        val currentConditions: CurrentCondition?
    ) {
        companion object {
            internal val DEFAULT = State(null)
        }
    }
}