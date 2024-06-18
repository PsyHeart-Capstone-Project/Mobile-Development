package com.capstone.psyheart.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.databinding.ActivityLoginBinding
import com.capstone.psyheart.model.LoginResponse
import com.capstone.psyheart.model.UserModel
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.main.MainActivity
import com.capstone.psyheart.ui.register.RegisterActivity
import com.capstone.psyheart.utils.ResultData

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButtonHandler()
        loginButtonHandler()
    }

    private fun registerButtonHandler() {
        binding.signupTv.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginButtonHandler() {
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                handlingLogin(email, password)
            } else {
                Toast.makeText(
                    this,
                    "Your email and password do not match, please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handlingLogin(email: String, password: String) {
        viewModel.login(email, password)
        //save email to shared preference
        val sharedPreference = this@LoginActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("email",email)
        editor.commit()


        viewModel.resultLogin.observe(this@LoginActivity) { result ->
            if (result != null) {
                when (result) {
                    is ResultData.Loading -> {
                        loadingHandler(true)
                    }

                    is ResultData.Failure -> {
                        loadingHandler(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultData.Success -> {
                        saveLoginData(result.data, email)
                        val sharedPreference = this@LoginActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
                        var editor = sharedPreference.edit()
                        editor.putString("token","Bearer "+result.data.data.token)
                        editor.commit()
                        navigateToHome()
                    }
                }
            }
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.root.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.root.visibility = View.VISIBLE
        }
    }

    private fun saveLoginData(loginResponse: LoginResponse, email: String) {
        val userPreference = UserPreference(this)
        val result = loginResponse.data
        Log.d("LoginActivity", "Saving email: $email") // Tambahkan log ini
        userPreference.setUser(
            UserModel(
                token = result.token
            )
        )
    }

    private fun navigateToHome() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
