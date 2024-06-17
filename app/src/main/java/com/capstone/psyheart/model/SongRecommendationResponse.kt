package com.capstone.psyheart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
//
//@Parcelize
//data class Recommendation(
//    @field:SerializedName("id")
//    val id: Int,
//
//    @field:SerializedName("name")
//    val name: String,
//
//    @field:SerializedName("duration")
//    val duration: String,
//
//    @field:SerializedName("artist_name")
//    val artist: String,
//
//    @field:SerializedName("url")
//    val url: String
//) : Parcelable