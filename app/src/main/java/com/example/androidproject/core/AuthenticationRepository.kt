package com.example.androidproject.core

import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    val loggedUser: String?
    fun validateAccount(email: String, password: String): Boolean
    fun saveAccount(email: String, password: String): Flow<Boolean>
    fun saveLoggedUser(email: String): Flow<Boolean>
    fun logoutUser()
    fun checkEmailAvailability(email: String): Boolean
}