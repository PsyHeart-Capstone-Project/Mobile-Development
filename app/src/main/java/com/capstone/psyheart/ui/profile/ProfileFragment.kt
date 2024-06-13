package com.capstone.psyheart.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.capstone.psyheart.databinding.FragmentProfileBinding
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.logout.LogoutActivity
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPreference: UserPreference // Deklarasi variabel untuk UserPreference

    companion object {
        const val LIGHT_THEME = 0
        const val DARK_THEME = 1
    }

    private var selectedLanguage = "in" // Inisialisasi dengan bahasa Indonesia sebagai default
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

        // Dapatkan data pengguna dari UserPreference
        val userModel = userPreference.getUser()

        // Debugging Log
        Log.d("ProfileFragment", "User Email: ${userModel.email}")

        // Atur teks pada profileTitleText dan descProfileText berdasarkan data pengguna
        binding.profileTitleText.text = userModel.name
        binding.descProfileText.text = userModel.email // Tampilkan email user
    }

    private fun setupViews() {
        binding.editAcc.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logoutTextView.setOnClickListener {
            val intent = Intent(requireContext(), LogoutActivity::class.java)
            startActivity(intent)
        }

        binding.languageDropDown.setEndIconOnClickListener {
            showLanguageSelectionDialog()
        }

        binding.themeDropDown.setEndIconOnClickListener {
            showThemeSelectionDialog()
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("Indonesian", "English")
        val checkedItem = if (selectedLanguage == "in") 0 else 1

        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                val newLanguage = if (which == 0) "in" else "en"
                if (selectedLanguage != newLanguage) {
                    selectedLanguage = newLanguage
                    saveSelectedLanguage()
                    updateAppLanguage()
                    requireActivity().recreate() // Restart the activity to apply language changes
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showThemeSelectionDialog() {
        val themes = arrayOf("Light Theme", "Dark Theme")
        val checkedItem = selectedTheme

        AlertDialog.Builder(requireContext())
            .setTitle("Select Theme")
            .setSingleChoiceItems(themes, checkedItem) { dialog, which ->
                selectedTheme = which
                saveSelectedTheme()
                updateAppTheme()
                requireActivity().recreate() // Restart the activity to apply theme changes
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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
