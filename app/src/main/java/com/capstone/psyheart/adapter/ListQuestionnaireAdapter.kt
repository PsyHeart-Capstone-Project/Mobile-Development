package com.capstone.psyheart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.psyheart.databinding.ItemQuestionnaireBinding
import com.capstone.psyheart.model.Questions

class ListQuestionnaireAdapter(private val questions: List<Questions>) : RecyclerView.Adapter<ListQuestionnaireAdapter.QuestionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionnaireBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    inner class QuestionViewHolder(private val binding: ItemQuestionnaireBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Questions) {
            binding.listQuestionnaire.text = question.questionText
        }
    }
}
