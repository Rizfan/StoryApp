package com.rizfan.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.pref.UserModel
import com.rizfan.storyapp.data.response.ListStoryItem

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    var isLoading: LiveData<Boolean> = repository.isLoading

    fun getStory(): LiveData<List<ListStoryItem>?> {
        repository.getStory()
        return repository.list
    }

    fun getSession(): LiveData<UserModel> {
        val r = repository.getSession().asLiveData()
        return r
    }

    suspend fun logout() {
        repository.logout()
    }

}