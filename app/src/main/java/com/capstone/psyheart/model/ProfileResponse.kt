package com.capstone.psyheart.model

data class ProfileResponse(
    val status: String,
    val message: String,
    val data: UserData
)

data class UserData(
    val user_id: String,
    val email: String,
    val name: String
)