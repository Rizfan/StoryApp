package com.rizfan.storyapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rizfan.storyapp.data.Result
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.response.StoryResponse

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    suspend fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> {
        return  repository.getStoriesWithLocation()
    }

}