package com.capstone.psyheart.ui.questionnaire

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.databinding.ActivityQuestionnaireBinding
import com.capstone.psyheart.ui.home.HomeFragment
import com.capstone.psyheart.ui.main.MainActivity

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.buttonSubmit.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this@QuestionnaireActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
