package com.capstone.psyheart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: RegisterResult,
) : Parcelable

@Parcelize
data class RegisterResult(
    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("token")
    val token: String
) : Parcelable