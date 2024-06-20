package com.capstone.psyheart.utils

import com.capstone.psyheart.constants.Constants

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validateMinPassword(password: String): Boolean {
    return password.length < Constants.MIN_PASSWORD_LENGTH
}