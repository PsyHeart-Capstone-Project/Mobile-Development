package com.capstone.psyheart.data

import com.capstone.psyheart.api.ApiService
import com.capstone.psyheart.model.SongCategoriesByIDResponse
import com.capstone.psyheart.model.SongCategoriesResponse
import com.capstone.psyheart.preference.UserPreference

class SongRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun getSongCategories(): SongCategoriesResponse {
        return apiService.songCategories("Bearer ${userPreference.getUser().token}")
    }

    suspend fun getSongDetailCategories(id: Int): SongCategoriesByIDResponse {
        return apiService.songCategoriesByID("Bearer ${userPreference.getUser().token}", id)
    }

    companion object {
        @Volatile
        private var instance: SongRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): SongRepository =
            instance ?: synchronized(this) {
                instance ?: SongRepository(apiService, userPreference)
            }.also { instance = it }
    }
}