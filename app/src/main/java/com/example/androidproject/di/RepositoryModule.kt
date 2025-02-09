package com.example.androidproject.di

import com.example.androidproject.core.AuthenticationRepository
import com.example.androidproject.core.TennisPlayersRepository
import com.example.androidproject.infra.AuthenticationRepositoryImpl
import com.example.androidproject.infra.TennisPlayersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindTennisPlayersRepository(
        tennisPlayersRepositoryImpl: TennisPlayersRepositoryImpl
    ): TennisPlayersRepository
}
