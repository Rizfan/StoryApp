package com.rizfan.storyapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rizfan.storyapp.data.StoryRepository
import com.rizfan.storyapp.data.pref.UserModel
import com.rizfan.storyapp.data.response.ListStoryItem

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    val getStory: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun logout() {
        repository.logout()
    }

}