package com.rizfan.storyapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rizfan.storyapp.data.Result
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: StoryRepository) : ViewModel() {

    suspend fun uploadStory(file: MultipartBody.Part, description: RequestBody, lat: Float, long: Float): LiveData<Result<UploadResponse>> {
        return repository.uploadImage(file, description, lat, long)
    }
}