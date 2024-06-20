package com.capstone.psyheart.data

import com.capstone.psyheart.api.ApiService
import com.capstone.psyheart.model.LoginResponse
import com.capstone.psyheart.model.ProfileResponse
import com.capstone.psyheart.model.RegisterResponse
import com.capstone.psyheart.model.UserModel
import com.capstone.psyheart.preference.UserPreference

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    private fun saveSession(user: UserModel) {
        userPreference.setUser(user)
    }

    suspend fun logout(token: String) {
        apiService.logout("Bearer $token")
        userPreference.logout()
    }

    suspend fun updateProfile(name: String, email: String, password: String): ProfileResponse {
        return apiService.editProfile(
            name,
            email,
            password,
            "Bearer ${userPreference.getUser().token}"
        )
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        val user = UserModel(
            name = response.loginResult.name,
            userId = response.loginResult.userId,
            token = response.loginResult.token,
            email = email
        )
        saveSession(user)
        return response
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}