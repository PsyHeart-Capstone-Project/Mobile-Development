package com.capstone.psyheart.di

import android.content.Context
import com.capstone.psyheart.api.ApiConfig
import com.capstone.psyheart.data.SongRepository
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.preference.UserPreference
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference(context)

        runBlocking { userPreference.getUser() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, userPreference)
    }

    fun provideSongRepository(context: Context): SongRepository {
        val userPreference = UserPreference(context)

        runBlocking { userPreference.getUser() }
        val apiService = ApiConfig.getApiService()
        return SongRepository.getInstance(apiService, userPreference)
    }
}