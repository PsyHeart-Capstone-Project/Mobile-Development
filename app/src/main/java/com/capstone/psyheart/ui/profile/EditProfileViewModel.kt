import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.psyheart.data.UserRepository
import com.capstone.psyheart.model.ErrorResponse
import com.capstone.psyheart.model.ProfileResponse
import com.capstone.psyheart.utils.ResultData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _resultProfile = MutableLiveData<ResultData<ProfileResponse>>()
    val resultProfile: LiveData<ResultData<ProfileResponse>> = _resultProfile

    fun updateProfile(userId: String, email: String, name: String, token: String) {
        viewModelScope.launch {
            _resultProfile.value = ResultData.Loading
            try {
                val response = userRepository.updateProfile(userId, email, name,token)
                _resultProfile.postValue(ResultData.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _resultProfile.postValue(ResultData.Failure(errorMessage.toString()))
            }
        }
    }
}