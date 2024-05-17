package com.rad21.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rad21.storyapp.data.UserRepository
import com.rad21.storyapp.data.response.ErrorResponse
import com.rad21.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val userRepository: UserRepository) :ViewModel(){

    private val _registration = MutableLiveData<RegisterResponse>()
    val registration: LiveData<RegisterResponse> = _registration

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isRegist = MutableLiveData<String>()
    val isRegist: LiveData<String> get() = _isRegist

    fun register(name: String, email: String, password: String)  {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = userRepository.register(name, email, password)
                _registration.postValue(response)
                _isLoading.postValue(false)
            } catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message.toString()
                _isRegist.postValue(errorMessage)

            }
        }

    }

}