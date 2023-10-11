package com.rizfan.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rizfan.storyapp.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: StoryRepository) : ViewModel() {

    var isLoading: LiveData<Boolean> = repository.isLoading

    fun uploadStory(file: MultipartBody.Part, description: RequestBody) {
        return repository.uploadImage(file, description)
    }
}