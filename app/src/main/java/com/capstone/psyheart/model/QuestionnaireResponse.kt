package com.capstone.psyheart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class QuestionnaireResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DataQuestions
)

data class DataQuestions(
    @field:SerializedName("questions")
    val questions: List<Questions> = emptyList()
)

@Parcelize
data class Questions(
    @field:SerializedName("question_id")
    val questionId: Int,

    @field:SerializedName("question_text")
    val questionText: String,

    @field:SerializedName("options")
    val options: List<String>
) : Parcelable
