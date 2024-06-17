package com.capstone.psyheart.ui.discover_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.psyheart.data.SongRepository
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.model.ErrorResponse
import com.capstone.psyheart.model.LoginResponse
import com.capstone.psyheart.model.SongCategoriesByIDResponse
import com.capstone.psyheart.model.SongCategoriesResponse
import com.capstone.psyheart.utils.ResultData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DiscoverDetailViewModel(private val repository: SongRepository) : ViewModel() {
    private val _resultSongs = MutableLiveData<ResultData<SongCategoriesByIDResponse>>()
    val resultSongs: LiveData<ResultData<SongCategoriesByIDResponse>> = _resultSongs
    fun getSongCategories(id: Int) {
        viewModelScope.launch {
            _resultSongs.value = ResultData.Loading
            viewModelScope.launch {
                try {
                    val response = repository.getSongDetailCategories(id)
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