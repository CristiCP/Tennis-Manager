package com.example.androidproject.di

import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitClient.retrofit.create(ApiService::class.java)
}