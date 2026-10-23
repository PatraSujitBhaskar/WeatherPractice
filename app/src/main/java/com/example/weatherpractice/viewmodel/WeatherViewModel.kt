package com.example.weatherpractice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.weatherpractice.WeatherRepository
import com.example.weatherpractice.database.WeatherEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel(){
    val weather: Flow<PagingData<WeatherEntity>> = weatherRepository.getWeather().cachedIn(viewModelScope)
}