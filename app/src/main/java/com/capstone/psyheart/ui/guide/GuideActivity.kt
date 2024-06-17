package com.capstone.psyheart.ui.guide

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.databinding.ActivityGuideBinding
import com.capstone.psyheart.ui.questionnaire.QuestionnaireActivity

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
            val isUpdateFlow = intent.getBooleanExtra(EXTRA_UPDATE_FLOW, false)

            if (isUpdateFlow) {
                val intent = Intent(this@GuideActivity, QuestionnaireActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@GuideActivity, QuestionnaireActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_UPDATE_FLOW = "extra_update_flow"
    }
}
