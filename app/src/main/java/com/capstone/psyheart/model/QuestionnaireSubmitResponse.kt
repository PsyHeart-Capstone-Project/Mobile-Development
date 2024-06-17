package com.capstone.psyheart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class QuestionnaireSubmitResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: QuestionnaireData
)

@Parcelize
data class QuestionnaireData(
    @field:SerializedName("mood")
    val mood: String,
) : Parcelable


//data class Questionnaire(
//    @field:SerializedName("questions")
//    val questions: List<Question>
//)
//
//data class Question(
//    @field:SerializedName("question_id")
//    val questionID: Int,
//
//    @field:SerializedName("question_text")
//    val questionText: String,
//
//    @field:SerializedName("options")
//    val options: List<String>
//)

data class Answer(
    @field:SerializedName("answers")
    val answers: List<AnswerBody>,
)

data class AnswerBody(
    @field:SerializedName("question_id")
    val questionID: Int,

    @field:SerializedName("answer")
    val answer: String,
)
