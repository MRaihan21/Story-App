package com.rad21.storyapp.data

import android.content.Context
import com.rad21.storyapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = com.rad21.storyapp.data.PreferenceManager.getInstance(context.dataStore)
        return UserRepository.getInstance(pref, apiService)
    }
}