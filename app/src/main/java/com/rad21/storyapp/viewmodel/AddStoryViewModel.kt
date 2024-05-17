package com.rad21.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rad21.storyapp.data.User
import com.rad21.storyapp.data.UserRepository
import com.rad21.storyapp.data.response.AddNewResponse
import com.rad21.storyapp.data.response.ErrorResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _uploadResponse = MutableLiveData<AddNewResponse>()
    val uploadResponse: LiveData<AddNewResponse> = _uploadResponse

    private val _isUpload = MutableLiveData<String>()
    val isUpload: LiveData<String> = _isUpload

    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userRepository.addStory(token, file, description)
                _uploadResponse.value = response
                _isLoading.value = false
                Log.d("AddViewModel", "addStory: ${Gson().toJson(response)}")
            } catch (e: HttpException) {
                _isLoading.value = false
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message.toString()
                _isUpload.postValue(errorMessage)
                Log.e("AddViewModel", "addStory: $errorMessage")
            }
        }
    }

    fun getSession(): LiveData<User> {
        return userRepository.getSession()
    }
}