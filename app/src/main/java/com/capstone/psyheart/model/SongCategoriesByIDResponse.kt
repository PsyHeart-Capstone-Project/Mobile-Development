package com.capstone.psyheart.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SongCategoriesByIDResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DataByID
)

data class DataByID(
    @field:SerializedName("detail")
    val detail: List<Detail> = emptyList(),

    @field:SerializedName("songs")
    val songs: List<Songs> = emptyList()
)

@Parcelize
data class Detail(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String
) : Parcelable

@Parcelize
data class Songs(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("duration")
    val duration: String,

    @field:SerializedName("url")
    val url: String
) : Parcelable