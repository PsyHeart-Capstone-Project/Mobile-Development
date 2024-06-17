package com.capstone.psyheart.data

import com.capstone.psyheart.api.ApiService
import com.capstone.psyheart.model.SongCategoriesResponse
import com.capstone.psyheart.preference.UserPreference

class QuestionnaireRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun getSongCategories(): SongCategoriesResponse {
        return apiService.songCategories("Bearer ${userPreference.getUser().token}")
    }

    companion object {
        @Volatile
        private var instance: QuestionnaireRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): QuestionnaireRepository =
            instance ?: synchronized(this) {
                instance ?: QuestionnaireRepository(apiService, userPreference)
            }.also { instance = it }
    }
}