package com.rizfan.storyapp.ui.model

import androidx.lifecycle.ViewModel
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.pref.UserModel

class LoginViewModel(private var repository: StoryRepository) : ViewModel() {

    suspend fun login(email: String, password: String) = repository.login(email, password)
    suspend fun saveSession(user: UserModel) {
        repository.saveSession(user)
    }

}