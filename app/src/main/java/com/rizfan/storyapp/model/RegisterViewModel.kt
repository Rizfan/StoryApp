package com.rizfan.storyapp.model

import androidx.lifecycle.ViewModel
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.response.RegisterResponse

class RegisterViewModel(private var repository: StoryRepository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name, email, password)
    }
}