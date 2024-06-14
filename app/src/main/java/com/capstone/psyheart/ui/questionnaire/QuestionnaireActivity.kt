package com.capstone.psyheart.ui.questionnaire

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.databinding.ActivityGuideBinding
import com.capstone.psyheart.databinding.ActivityQuestionnaireBinding

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  setupAction()
    }

    /*private fun nextHandler() {
        binding.buttonNext.setOnClickListener {
            val intent = Intent(this@GuideActivity, ::class.java)
            startActivity(intent)
            finish()
        }
    }*/
}
