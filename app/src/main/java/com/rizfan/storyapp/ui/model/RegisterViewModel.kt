package com.rizfan.storyapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rizfan.storyapp.data.Result
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.response.RegisterResponse

class RegisterViewModel(private var repository: StoryRepository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> {
        return repository.register(name, email, password)
    }
}