package com.rad21.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rad21.storyapp.data.User
import com.rad21.storyapp.data.UserRepository
import com.rad21.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _uploadResponse = MutableLiveData<StoryResponse>()
    val uploadResponse: LiveData<StoryResponse> get() = _uploadResponse
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAllStories(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userRepository.getAllStories(token)
                _uploadResponse.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun getSession(): LiveData<User> {
        return userRepository.getSession()
    }

}