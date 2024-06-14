package com.capstone.psyheart.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.model.ErrorResponse
import com.capstone.psyheart.model.LoginResponse
import com.capstone.psyheart.utils.ResultData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _resultLogin = MutableLiveData<ResultData<LoginResponse>>()
    val resultLogin: LiveData<ResultData<LoginResponse>> = _resultLogin
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _resultLogin.value = ResultData.Loading
            viewModelScope.launch {
                try {
                    val response = userRepository.login(email, password)
                    _resultLogin.postValue(ResultData.Success(response))
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val errorMessage = errorBody.message
                    _resultLogin.postValue(ResultData.Failure(errorMessage.toString()))
                }
            }
        }
    }
}