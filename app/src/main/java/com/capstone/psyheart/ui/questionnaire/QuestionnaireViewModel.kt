package com.capstone.psyheart.ui.questionnaire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.psyheart.data.SongRepository
import com.capstone.psyheart.model.AnswerBody
import com.capstone.psyheart.model.ErrorResponse
import com.capstone.psyheart.model.QuestionnaireResponse
import com.capstone.psyheart.model.QuestionnaireSubmitResponse
import com.capstone.psyheart.utils.ResultData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class QuestionnaireViewModel(private val repository: SongRepository) : ViewModel() {
    private val _resultQuestionnaire = MutableLiveData<ResultData<QuestionnaireResponse>>()
    val resultQuestionnaire: LiveData<ResultData<QuestionnaireResponse>> = _resultQuestionnaire
    fun getQuestionnaire() {
        viewModelScope.launch {
            _resultQuestionnaire.value = ResultData.Loading
            viewModelScope.launch {
                try {
                    val response = repository.getQuestionnaire()
                    _resultQuestionnaire.postValue(ResultData.Success(response))
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val errorMessage = errorBody.message
                    _resultQuestionnaire.postValue(ResultData.Failure(errorMessage.toString()))
                }
            }
        }
    }

    private val _resultSubmitQuestionnaire =
        MutableLiveData<ResultData<QuestionnaireSubmitResponse>>()
    val resultSubmitQuestionnaire: LiveData<ResultData<QuestionnaireSubmitResponse>> =
        _resultSubmitQuestionnaire

    fun submitQuestionnaire(isNewUser: Boolean, answers: List<AnswerBody>) {
        viewModelScope.launch {
            _resultSubmitQuestionnaire.value = ResultData.Loading
            viewModelScope.launch {
                try {
                    val response: QuestionnaireSubmitResponse = if (isNewUser) {
                        repository.postQuestionnaire(answers)
                    } else {
                        repository.putQuestionnaire(answers)
                    }

                    _resultSubmitQuestionnaire.postValue(ResultData.Success(response))
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val errorMessage = errorBody.message
                    _resultSubmitQuestionnaire.postValue(ResultData.Failure(errorMessage.toString()))
                }
            }
        }
    }
}