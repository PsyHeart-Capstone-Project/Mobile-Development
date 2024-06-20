package com.capstone.psyheart.model

import com.google.gson.annotations.SerializedName

data class SongRecommendationResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DataRecommendation
)

data class DataRecommendation(
    @field:SerializedName("mood")
    val mood: String,

    @field:SerializedName("recommendation")
    val recommendation: List<Songs> = emptyList()
)