package com.rizfan.storyapp.di

import android.content.Context
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.pref.UserPreference
import com.rizfan.storyapp.data.pref.dataStore
import com.rizfan.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository = runBlocking  {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getSession().first()
        val apiService = ApiConfig.getApiService(user.token)
        StoryRepository.getInstance(apiService, pref)
    }

}