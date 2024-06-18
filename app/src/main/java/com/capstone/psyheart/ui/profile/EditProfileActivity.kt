package com.capstone.psyheart.ui.profile

import EditProfileViewModel
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.psyheart.R
import com.capstone.psyheart.databinding.ActivityEditProfileBinding
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.login.LoginViewModel
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var isPasswordVisible = false
    private var isPasswordVisibleconfirm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        enableEdgeToEdge()
        setupViews()
    }

    private fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.icVisible.setOnClickListener {
            togglePasswordVisibility()
        }
        binding.icVisibleConfirm.setOnClickListener {
            togglePasswordConfirmVisibility()
        }


        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvSaveChanges.setOnClickListener {
            val name = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

            } else if (password != confirmPassword) {

            } else {
                val sharedPreference = this@EditProfileActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
                lifecycleScope.launch {
                    viewModel.updateProfile(name, email, password, sharedPreference.getString("token", "")!!).let {
                        Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.icVisible.setImageResource(com.chuckerteam.chucker.R.drawable.design_ic_visibility_off)
        } else {
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.icVisible.setImageResource(R.drawable.ic_visible)
        }
        isPasswordVisible = !isPasswordVisible
        binding.etPassword.setSelection(binding.etPassword.text!!.length) // Move cursor to the end
    }
    private fun togglePasswordConfirmVisibility() {
        if (isPasswordVisibleconfirm) {
            binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.icVisibleConfirm.setImageResource(com.chuckerteam.chucker.R.drawable.design_ic_visibility_off)
        } else {
            binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.icVisibleConfirm.setImageResource(R.drawable.ic_visible)
        }
        isPasswordVisibleconfirm = !isPasswordVisibleconfirm
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text!!.length) // Move cursor to the end
    }
}