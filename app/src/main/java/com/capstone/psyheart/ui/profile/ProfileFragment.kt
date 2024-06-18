package com.capstone.psyheart.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.psyheart.R
import com.capstone.psyheart.api.ApiService
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.databinding.FragmentProfileBinding
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.login.LoginViewModel
import com.capstone.psyheart.ui.logout.LogoutActivity
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPreference: UserPreference // Deklarasi variabel untuk UserPreference
    //viewmodel
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    companion object {
        const val LIGHT_THEME = 0
        const val DARK_THEME = 1
    }

    private var selectedLanguage = "en" // Inisialisasi dengan bahasa Indonesia sebagai default
    private var selectedTheme = LIGHT_THEME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreference = UserPreference(requireContext()) // Inisialisasi UserPreference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        loadSelectedLanguage()
        loadSelectedTheme()
        updateAppLanguage()
        updateAppTheme()
        setupViews()
        showLanguageSelectionDialog()
        showThemeSelectionDialog()

        // Dapatkan data pengguna dari UserPreference
        val userModel = userPreference.getUser()


        // Atur teks pada profileTitleText dan descProfileText berdasarkan data pengguna
        binding.profileTitleText.text = "PsyHeart"
        //get from shared preference

        val sharedPreferences2 = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        binding.descProfileText.text =  sharedPreferences2.getString("email", "defaultEmail")

    }

    private fun setupViews() {
        binding.editAcc.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logoutTextView.setOnClickListener {
            userPreference.logout()
            val sharedPreference = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)

            viewModel.logout(sharedPreference.getString("token", "asdasd")!!)

            val intent = Intent(requireContext(), LogoutActivity::class.java)
            startActivity(intent)
        }


    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English","Indonesian")
        val checkedItem = if (selectedLanguage == "in") 1 else 0
        val spinner = binding.spinner
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val newLanguage = if (position == 0) "en" else "in"
                if (selectedLanguage != newLanguage) {
                    selectedLanguage = newLanguage
                    saveSelectedLanguage()
                    updateAppLanguage()
                    requireActivity().recreate() // Restart the activity to apply language changes
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


    }

    private fun showThemeSelectionDialog() {
        val themes = arrayOf("Light Theme", "Dark Theme")
        val checkedItem = selectedTheme
        val spinner = binding.themespinner
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themes)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val newTheme = if (position == 0) LIGHT_THEME else DARK_THEME
                if (selectedTheme != newTheme) {
                    selectedTheme = newTheme
                    saveSelectedTheme()
                    updateAppTheme()
                    requireActivity().recreate() // Restart the activity to apply theme changes
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }


    private fun updateAppLanguage() {
        val locale = if (selectedLanguage == "in") Locale("id") else Locale.ENGLISH
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun updateAppTheme() {
        when (selectedTheme) {
            LIGHT_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            DARK_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun saveSelectedLanguage() {
        sharedPreferences.edit().putString("selected_language", selectedLanguage).apply()
    }

    private fun saveSelectedTheme() {
        sharedPreferences.edit().putInt("selected_theme", selectedTheme).apply()
    }

    private fun loadSelectedLanguage() {
        selectedLanguage = sharedPreferences.getString("selected_language", "in") ?: "in"
    }

    private fun loadSelectedTheme() {
        selectedTheme = sharedPreferences.getInt("selected_theme", LIGHT_THEME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
