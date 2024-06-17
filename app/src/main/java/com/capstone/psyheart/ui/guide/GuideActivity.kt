package com.capstone.psyheart.ui.guide

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.databinding.ActivityGuideBinding
import com.capstone.psyheart.ui.questionnaire.QuestionnaireActivity
import com.capstone.psyheart.ui.register.RegisterActivity

class GuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nextHandler()
    }

    private fun nextHandler() {
        binding.buttonNext.setOnClickListener {
            val isNewUser = intent.getBooleanExtra(RegisterActivity.IS_NEW_USER, false)
            val intent = Intent(this@GuideActivity, QuestionnaireActivity::class.java).apply {
                putExtra(RegisterActivity.IS_NEW_USER, isNewUser)
            }
            startActivity(intent)
            finish()
        }
    }

}
