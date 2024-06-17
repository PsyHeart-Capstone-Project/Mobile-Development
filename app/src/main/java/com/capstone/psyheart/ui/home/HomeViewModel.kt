package com.capstone.psyheart.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.psyheart.data.SongRepository
import com.capstone.psyheart.model.ErrorResponse
import com.capstone.psyheart.model.SongCategoriesByIDResponse
import com.capstone.psyheart.model.SongRecommendationResponse
import com.capstone.psyheart.utils.ResultData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val repository: SongRepository) : ViewModel() {

    private val _resultSongs = MutableLiveData<ResultData<SongRecommendationResponse>>()
    val resultSongs: LiveData<ResultData<SongRecommendationResponse>> = _resultSongs
    fun getSongRecommendation() {
        viewModelScope.launch {
            _resultSongs.value = ResultData.Loading
            viewModelScope.launch {
                try {
                    val response = repository.getSongRecommendation()
                    _resultSongs.postValue(ResultData.Success(response))
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val errorMessage = errorBody.message
                    _resultSongs.postValue(ResultData.Failure(errorMessage.toString()))
                }
            }
        }
    }
}