package com.capstone.psyheart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class QuestionnairePutResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DataUpdate
)

@Parcelize
data class DataUpdate(
    @field:SerializedName("mood")
    val mood: String,

    @field:SerializedName("description")
    val description: String
) : Parcelable
