package com.example.weatherpractice.di

import com.example.weatherpractice.data.repository.WeatherRepositoryImpl
import com.example.weatherpractice.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(implementation: WeatherRepositoryImpl): WeatherRepository
}