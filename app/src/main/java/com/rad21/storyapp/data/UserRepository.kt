package com.rad21.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.rad21.storyapp.data.response.AddNewResponse
import com.rad21.storyapp.data.response.RegisterResponse
import com.rad21.storyapp.data.response.StoryResponse
import com.rad21.storyapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository (private val preferenceManager: com.rad21.storyapp.data.PreferenceManager, private val apiService: ApiService) {

    suspend fun login(email: String, password: String) =
        apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) : RegisterResponse {
        Log.d("Register", "$name, $email, $password")
        return apiService.register(name, email, password)
    }

    suspend fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) : AddNewResponse {
        return apiService.addStory(token, file, description)
    }

    suspend fun getAllStories(token: String) : StoryResponse {
        return apiService.getAllStories(token)
    }

    fun getSession() : LiveData<User> {
        return preferenceManager.getSession().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: com.rad21.storyapp.data.PreferenceManager,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }

}
