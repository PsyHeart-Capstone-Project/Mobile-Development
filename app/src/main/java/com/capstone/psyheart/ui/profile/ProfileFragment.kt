package com.capstone.psyheart.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.psyheart.R
import com.capstone.psyheart.databinding.FragmentProfileBinding
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.logout.LogoutActivity
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPreference: UserPreference

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    companion object {
        const val LIGHT_THEME = 0
        const val DARK_THEME = 1
    }

    private var selectedLanguage = "en"
    private var selectedTheme = LIGHT_THEME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreference = UserPreference(requireContext())
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        loadSelectedLanguage()
        loadSelectedTheme()
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
        applyInitialSettings()
        setupViews()
        setupLanguageSpinner()
        setupThemeSpinner()
        binding.descProfileText.text = userPreference.getUser().email
    }

    private fun setupViews() {
        binding.editAcc.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logoutTextView.setOnClickListener {
            userPreference.getUser().token?.let { token -> viewModel.logout(token) }
            val intent = Intent(requireContext(), LogoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLanguageSpinner() {
        val languages = arrayOf(getString(R.string.english), getString(R.string.indonesian))
        val arrayAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            languages
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(
                        resources.getColor(
                            R.color.secondaryColor,
                            null
                        )
                    )
                }
                return view
            }

        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val newLanguage = if (position == 1) "in" else "en"
                if (selectedLanguage != newLanguage) {
                    selectedLanguage = newLanguage
                    saveSelectedLanguage()
                    updateAppLanguage()
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun setupThemeSpinner() {
        val themes = arrayOf(getString(R.string.light_theme), getString(R.string.dark_theme))
        val arrayAdapter = object :
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, themes) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(
                        resources.getColor(
                            R.color.secondaryColor,
                            null
                        )
                    )
                }
                return view
            }

        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.themespinner.adapter = arrayAdapter
        binding.themespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val newTheme = if (position == 0) LIGHT_THEME else DARK_THEME
                if (selectedTheme != newTheme) {
                    selectedTheme = newTheme
                    saveSelectedTheme()
                    updateAppTheme()
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }

    private fun applyInitialSettings() {
        updateAppLanguage()
        updateAppTheme()
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
            LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun saveSelectedLanguage() {
        sharedPreferences.edit().putString("selected_language", selectedLanguage).apply()
    }

    private fun saveSelectedTheme() {
        sharedPreferences.edit().putInt("selected_theme", selectedTheme).apply()
    }

    private fun loadSelectedLanguage() {
        selectedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"
    }

    private fun loadSelectedTheme() {
        selectedTheme = sharedPreferences.getInt("selected_theme", LIGHT_THEME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}