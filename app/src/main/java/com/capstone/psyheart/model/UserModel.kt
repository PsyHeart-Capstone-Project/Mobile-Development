package com.capstone.psyheart.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
data class UserModel (
    var userId: String? = null,
    var name: String? = null,
    var token: String? = null
) : Parcelable