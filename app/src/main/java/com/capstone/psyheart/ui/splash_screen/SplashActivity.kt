package com.capstone.psyheart.ui.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.constants.Constants
import com.capstone.psyheart.databinding.ActivitySplashBinding
import com.capstone.psyheart.model.UserModel
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.login.LoginActivity
import com.capstone.psyheart.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var userPreference: UserPreference
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPreference()
        splashScreenHandler()
        nextButtonHandler()
    }

    private fun setupPreference() {
        userPreference = UserPreference(this)
        userModel = userPreference.getUser()
    }

    private fun splashScreenHandler() {
        if (userModel.token != "null") {
            val intent = Intent(this, MainActivity::class.java)
            navigate(intent)
        }
    }

    private fun navigate(intent: Intent) {
        startActivity(intent)
        finish()
        /*val splashTimer: Long = Constants.SPLASH_TIMER
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, splashTimer)*/
    }

    private fun nextButtonHandler() {
        binding.start.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            navigate(intent)
        }
    }

}