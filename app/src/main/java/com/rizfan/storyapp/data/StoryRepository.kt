package com.rizfan.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.rizfan.storyapp.data.database.StoryDatabase
import com.rizfan.storyapp.data.pref.UserModel
import com.rizfan.storyapp.data.pref.UserPreference
import com.rizfan.storyapp.data.response.ListStoryItem
import com.rizfan.storyapp.data.response.LoginResponse
import com.rizfan.storyapp.data.response.RegisterResponse
import com.rizfan.storyapp.data.response.StoryResponse
import com.rizfan.storyapp.data.response.UploadResponse
import com.rizfan.storyapp.data.retrofit.ApiService
import com.rizfan.storyapp.data.utils.StoryRemoteMediator
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(config = PagingConfig(
            pageSize = 5
        ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        val response = apiService.register(name, email, password)
        if (!response.error){
            emit(Result.Success(response))
        }else{
            emit(Result.Error(response.message))
        }
    }

    suspend fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(Result.Error(errorBody.message.toString()))
        }
    }

    suspend fun getStoriesWithLocation() : LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            if (response.error == false){
                emit(Result.Success(response))
            }else{
                emit(Result.Error(response.message.toString()))
            }
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun uploadImage(file: MultipartBody.Part, description: RequestBody, lat: Float, lon: Float) : LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        val response = if(lat != 0f && lon != 0f) {
            apiService.uploadImageWithLocation(file, description, lat, lon)
        } else {
            apiService.uploadImage(file, description)
        }

        if (response.error == false){
            emit(Result.Success(response))
        }else{
            emit(Result.Error(response.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun clearInstance() {
            instance = null
        }

        fun getInstance(
            database: StoryDatabase,
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(database, apiService, userPreference)
            }.also { instance = it }
    }
}