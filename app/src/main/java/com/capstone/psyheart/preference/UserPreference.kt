package com.capstone.psyheart.preference

import android.content.Context
import android.util.Log
import com.capstone.psyheart.model.UserModel

class UserPreference(context: Context) {
    private val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserModel) {
        val editor = prefs.edit()
        editor.apply {
            putString(USER_NAME, value.name)
            putString(USER_ID, value.userId)
            putString(USER_TOKEN, value.token)
            putString(USER_EMAIL, value.email) // Menyimpan email user
            apply()
        }
        Log.d("UserPreference", "Email disimpan: ${value.email}") // Tambahkan log ini
    }

    fun getUser(): UserModel {
        val name = prefs.getString(USER_NAME, null)
        val userId = prefs.getString(USER_ID, null)
        val token = prefs.getString(USER_TOKEN, null)
        val email = prefs.getString(USER_EMAIL, null) // Mengambil email user
        Log.d("UserPreference", "Email diambil: $email") // Tambahkan log ini
        return UserModel(userId, name, token, email)
    }

    fun logout() {
        val editor = prefs.edit().clear()
        editor.apply()
    }

    companion object {
        private const val PREFERENCE_NAME = "user_preference"
        private const val USER_ID = "id"
        private const val USER_NAME = "name"
        private const val USER_TOKEN = "token"
        private const val USER_EMAIL = "email" // Key untuk email
    }
}
