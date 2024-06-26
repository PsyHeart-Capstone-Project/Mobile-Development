package com.capstone.psyheart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("data")
    val loginResult: LoginResult,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class LoginResult(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("email")
    val email: String
) : Parcelable