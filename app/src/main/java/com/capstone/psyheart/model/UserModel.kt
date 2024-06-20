package com.capstone.psyheart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var userId: String? = null,
    var name: String? = null,
    var token: String? = null,
    var email: String? = null,
    var lang: String? = null,
    var theme: Int = 99
) : Parcelable