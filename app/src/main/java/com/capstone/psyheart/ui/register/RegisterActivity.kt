package com.capstone.psyheart.ui.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.R
import com.capstone.psyheart.databinding.ActivityRegisterBinding
import com.capstone.psyheart.model.RegisterResponse
import com.capstone.psyheart.model.UserModel
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.main.MainActivity
import com.capstone.psyheart.utils.ResultData

class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val sharedPreference = this@RegisterActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()

            editor.putString("email",email)
            editor.commit()

            viewModel.register(name, email, password)
        }

        viewModel.registerResult.observe(this@RegisterActivity) { result ->
            if (result != null) {
                when (result) {
                    is ResultData.Loading -> {
                        showLoading(true)
                    }

                    is ResultData.Failure -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultData.Success -> {
                        showLoading(false)
                        Toast.makeText(
                            this,
                            "${getString(R.string.congrats)} ${result.data.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveLoginData(result.data)
                        val sharedPreference = this@RegisterActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
                        var editor = sharedPreference.edit()
                        editor.putString("token","Bearer "+result.data.data.token)
                        editor.commit()
                        navigateToHome()
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveLoginData(registerResponse: RegisterResponse) {
        val userPreference = UserPreference(this)
        val result = registerResponse.data
        userPreference.setUser(
            UserModel(

                token = result.token
            )
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}