package com.rizfan.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.pref.UserModel
import com.rizfan.storyapp.data.response.LoginResponse

class LoginViewModel(private var repository: StoryRepository) : ViewModel() {

    var loginResponse: MutableLiveData<LoginResponse> = repository.loginResponse

    var isLoading: LiveData<Boolean> = repository.isLoading

    fun login(email: String, password: String) {
        return repository.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        repository.saveSession(user)
    }

}