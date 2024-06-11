package com.capstone.psyheart.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.capstone.psyheart.R
import com.capstone.psyheart.databinding.FragmentProfileBinding
import com.capstone.psyheart.ui.logout.LogoutActivity
import com.capstone.psyheart.ui.profile.EditProfileActivity
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var selectedLanguage = "en" // Inisialisasi dengan bahasa Inggris sebagai default
    private lateinit var sharedPreferences: SharedPreferences

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
        updateAppLanguage()
        setupViews()
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
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("Indonesian", "English")
        val checkedItem = if (selectedLanguage == "in") 0 else 1

        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                selectedLanguage = if (which == 0) "in" else "en"
                saveSelectedLanguage()
                updateAppLanguage()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateAppLanguage() {
        val locale = if (selectedLanguage == "en") Locale.ENGLISH else Locale("in")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        requireActivity().recreate() // Memanggil recreate() untuk menerapkan perubahan bahasa
    }

    private fun saveSelectedLanguage() {
        sharedPreferences.edit().putString("selected_language", selectedLanguage).apply()
    }

    private fun loadSelectedLanguage() {
        selectedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}