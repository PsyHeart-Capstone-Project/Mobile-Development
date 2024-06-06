package com.capstone.psyheart.di

import android.content.Context
import com.capstone.psyheart.api.ApiConfig
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.preference.UserPreference
import kotlinx.coroutines.runBlocking

// private val Context.dataStore by preferencesDataStore(name = "settings")
object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        // val dataStore = context.dataStore
        val userPreference = UserPreference(context)

        runBlocking { userPreference.getUser() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, userPreference)
    }
}