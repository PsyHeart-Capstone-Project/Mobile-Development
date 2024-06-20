package com.capstone.psyheart.ui.register

import android.content.Intent
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
import com.capstone.psyheart.ui.guide.GuideActivity
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

            viewModel.register(name, email, password)
        }

        viewModel.registerResult.observe(this@RegisterActivity) { result ->
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
                    navigateToGuide() // Navigate to GuideActivity after successful registration
                }
            }
        }
    }

    private fun navigateToGuide() {
        val intent = Intent(this@RegisterActivity, GuideActivity::class.java).apply {
            putExtra(IS_NEW_USER, true)
        }
        startActivity(intent)
        finish()
    }

    private fun saveLoginData(registerResponse: RegisterResponse) {
        val userPreference = UserPreference(this)
        val result = registerResponse.data
        userPreference.setUser(
            UserModel(
                name = binding.registerName.text.toString(),
                userId = result.userId,
                token = result.token,
                email = binding.registerEmail.text.toString()
            )
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val IS_NEW_USER = "is_new_user"
    }
}