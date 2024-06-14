package com.capstone.psyheart.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.psyheart.data.SongRepository
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.model.ErrorResponse
import com.capstone.psyheart.model.LoginResponse
import com.capstone.psyheart.model.SongCategoriesResponse
import com.capstone.psyheart.utils.ResultData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DiscoverViewModel(private val repository: SongRepository) : ViewModel() {
    private val _resultSongCategories = MutableLiveData<ResultData<SongCategoriesResponse>>()
    val resultSongCategories: LiveData<ResultData<SongCategoriesResponse>> = _resultSongCategories
    fun getSongCategories() {
        viewModelScope.launch {
            _resultSongCategories.value = ResultData.Loading
            viewModelScope.launch {
                try {
                    val response = repository.getSongCategories()
                    _resultSongCategories.postValue(ResultData.Success(response))
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val errorMessage = errorBody.message
                    _resultSongCategories.postValue(ResultData.Failure(errorMessage.toString()))
                }
            }
        }
    }
}