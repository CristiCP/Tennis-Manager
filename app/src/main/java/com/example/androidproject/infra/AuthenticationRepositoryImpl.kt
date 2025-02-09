package com.example.androidproject.infra

import android.content.Context
import android.content.SharedPreferences
import com.example.androidproject.core.AuthenticationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : AuthenticationRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(FILE_NAME_ACCOUNTS, Context.MODE_PRIVATE)

    override val loggedUser: String?
        get() = sharedPreferences.getString(KEY_LOGGED_USER, null)

    override fun validateAccount(email: String, password: String): Boolean {
        val accounts = sharedPreferences.getString(
            KEY_ACCOUNTS,
            ""
        )?.lines()
        return accounts?.any { it == "$email:$password" } == true
    }

    override fun checkEmailAvailability(email: String): Boolean {
        val accounts = sharedPreferences.getString(KEY_ACCOUNTS, "")?.lines()
        return accounts?.any { it.startsWith("$email:") } == true
    }

    override fun saveAccount(email: String, password: String): Flow<Boolean> = flow {
        delay(DELAY)
        val accountsData = sharedPreferences.getString(KEY_ACCOUNTS, "")
        val updatedAccountsData = if (accountsData.isNullOrBlank()) {
            "$email:$password"
        } else {
            "$accountsData\n$email:$password"
        }
        val isSuccessful =
            sharedPreferences.edit().putString(KEY_ACCOUNTS, updatedAccountsData).commit()
        emit(isSuccessful)
    }

    override fun saveLoggedUser(email: String): Flow<Boolean> = flow {
        delay(DELAY)
        val isSuccessful = sharedPreferences.edit().putString(KEY_LOGGED_USER, email).commit()
        emit(isSuccessful)
    }

    override fun logoutUser() {
        sharedPreferences.edit().remove(KEY_LOGGED_USER).apply()
    }

    companion object {
        private const val FILE_NAME_ACCOUNTS = "userData"
        private const val KEY_ACCOUNTS = "accounts"
        private const val KEY_LOGGED_USER = "userLogged"
        private const val DELAY: Long = 3000
    }
}