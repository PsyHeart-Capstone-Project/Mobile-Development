package com.capstone.psyheart.ui.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.psyheart.R
import com.capstone.psyheart.databinding.ActivityEditProfileBinding
import com.capstone.psyheart.model.UserModel
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.utils.ResultData
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var loadingDialog: Dialog? = null
    private var isPasswordVisible = false
    private var isPasswordVisibleConfirm = false
    private lateinit var name: String
    private lateinit var email: String
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
            name = binding.etFullName.text.toString()
            email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    this@EditProfileActivity,
                    R.string.fill_required,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password != confirmPassword) {
                Toast.makeText(
                    this@EditProfileActivity,
                    R.string.pwd_not_match,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    viewModel.updateProfile(name, email, password)
                }
            }
        }

        viewModel.resultProfile.observe(this@EditProfileActivity) { result ->
            if (result != null) {
                when (result) {
                    is ResultData.Loading -> {
                        loadingHandler(true)
                    }

                    is ResultData.Failure -> {
                        loadingHandler(false)
                        Toast.makeText(this@EditProfileActivity, result.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ResultData.Success -> {
                        loadingHandler(false)
                        saveLoginData(result.data.data.name, result.data.data.email)
                        Toast.makeText(
                            this@EditProfileActivity,
                            R.string.success_update_profile,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            showLoadingPopup()
        } else {
            hideLoadingPopup()
        }
    }

    @SuppressLint("InflateParams")
    private fun showLoadingPopup() {
        if (loadingDialog == null) {
            loadingDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar).apply {
                val view: View = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
                setContentView(view)
                setCancelable(false)
            }
        }
        loadingDialog?.show()
    }

    private fun hideLoadingPopup() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
    }

    private fun saveLoginData(name: String, email: String) {
        val userPreference = UserPreference(this)
        userPreference.updateUser(
            UserModel(
                name = name, email = email
            )
        )
    }

    @SuppressLint("PrivateResource")
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.icVisible.setImageResource(com.chuckerteam.chucker.R.drawable.design_ic_visibility_off)
        } else {
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.icVisible.setImageResource(R.drawable.ic_visible)
        }
        isPasswordVisible = !isPasswordVisible
        binding.etPassword.setSelection(binding.etPassword.text!!.length)
    }

    @SuppressLint("PrivateResource")
    private fun togglePasswordConfirmVisibility() {
        if (isPasswordVisibleConfirm) {
            binding.etConfirmPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.icVisibleConfirm.setImageResource(com.chuckerteam.chucker.R.drawable.design_ic_visibility_off)
        } else {
            binding.etConfirmPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.icVisibleConfirm.setImageResource(R.drawable.ic_visible)
        }
        isPasswordVisibleConfirm = !isPasswordVisibleConfirm
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text!!.length)
    }
}