package com.capstone.psyheart.ui.questionnaire

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.psyheart.R
import com.capstone.psyheart.adapter.QuestionnaireAdapter
import com.capstone.psyheart.databinding.ActivityQuestionnaireBinding
import com.capstone.psyheart.model.Questions
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.main.MainActivity
import com.capstone.psyheart.ui.register.RegisterActivity.Companion.IS_NEW_USER
import com.capstone.psyheart.utils.ResultData

class QuestionnaireActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionnaireBinding
    private val viewModel by viewModels<QuestionnaireViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: QuestionnaireAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listQuestionnaire.layoutManager = LinearLayoutManager(this)
        adapter = QuestionnaireAdapter()
        binding.listQuestionnaire.adapter = adapter

        val isNewUser = intent.getBooleanExtra(IS_NEW_USER, false)

        viewModel.getQuestionnaire()

        binding.buttonSubmit.setOnClickListener {
            val answers = adapter.getAnswers()
            viewModel.submitQuestionnaire(isNewUser, answers)
        }

        handleQuestionnaire()
        handleSubmitQuestionnaire()
    }

    private fun navigateToHome() {
        val intent = Intent(this@QuestionnaireActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleQuestionnaire() {
        viewModel.resultQuestionnaire.observe(this@QuestionnaireActivity) { result ->
            if (result != null) {
                when (result) {
                    is ResultData.Loading -> {
                        loadingHandler(true)
                    }

                    is ResultData.Failure -> {
                        loadingHandler(false)
                    }

                    is ResultData.Success -> {
                        loadingHandler(false)
                        setupView(this@QuestionnaireActivity, result.data.data.questions)
                    }
                }
            }
        }
    }

    private fun handleSubmitQuestionnaire() {
        viewModel.resultSubmitQuestionnaire.observe(this@QuestionnaireActivity) { result ->
            if (result != null) {
                when (result) {
                    is ResultData.Loading -> {
                        loadingHandler(true)
                    }

                    is ResultData.Failure -> {
                        Toast.makeText(
                            this,
                            "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingHandler(false)
                    }

                    is ResultData.Success -> {
                        loadingHandler(false)
                        Toast.makeText(
                            this,
                            getString(R.string.home_title_msg) + " " + result.data.data.mood,
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHome()
                    }
                }
            }
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        // Handle loading state (e.g., show/hide progress bar)
    }

    private fun setupView(context: Context, questions: List<Questions>) {
        val rv = binding.listQuestionnaire
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.submitList(questions)
    }
}