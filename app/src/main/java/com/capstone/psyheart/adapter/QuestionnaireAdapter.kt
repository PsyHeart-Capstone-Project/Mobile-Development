package com.capstone.psyheart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.capstone.psyheart.databinding.ItemQuestionnaireBinding
import com.capstone.psyheart.model.AnswerBody
import com.capstone.psyheart.model.Questions

class QuestionnaireAdapter : RecyclerView.Adapter<QuestionnaireAdapter.QuestionViewHolder>() {
    private var questions: List<Questions> = emptyList()
    private val answers: MutableMap<Int, String> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionnaireBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questions.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(questions: List<Questions>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    fun getAnswers(): List<AnswerBody> {
        val sortedAnswers = answers.toList().sortedBy { it.first }
        return sortedAnswers.map { AnswerBody(it.first, it.second) }
    }
    inner class QuestionViewHolder(private val binding: ItemQuestionnaireBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Questions) {
            binding.listQuestionnaire.text = question.questionText
            binding.radioGroupCondition.removeAllViews()
            question.options.forEach { option ->
                val radioButton = RadioButton(binding.radioGroupCondition.context).apply {
                    text = option
                    setOnClickListener {
                        answers[question.questionId] = option
                    }
                }
                binding.radioGroupCondition.addView(radioButton)
            }
        }
    }
}
