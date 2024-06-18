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

    suspend fun saveSession(user: UserModel) {
        userPreference.setUser(user)
    }

    fun getSession(): UserModel {
        return userPreference.getUser()
    }

    suspend fun logout(token:String) {
        apiService.logout(token)
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        val user = UserModel(

            token = response.data.token,
        )
        saveSession(user)
        return response
    }
    suspend fun updateProfile(name: String, email: String, password: String,token: String): ProfileResponse {
        return apiService.editprofile(name, email, password, "$token")
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
