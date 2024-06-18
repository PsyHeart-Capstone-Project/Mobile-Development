package com.capstone.psyheart.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserModel (
    val token: String
) : Parcelable
