package com.rizfan.storyapp.data.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rizfan.storyapp.data.database.StoryDatabase
import com.rizfan.storyapp.data.response.ListStoryItem
import com.rizfan.storyapp.data.response.LoginResponse
import com.rizfan.storyapp.data.response.RegisterResponse
import com.rizfan.storyapp.data.response.StoryResponse
import com.rizfan.storyapp.data.response.UploadResponse
import com.rizfan.storyapp.data.retrofit.ApiService
import junit.framework.TestCase.*
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest{

    private var mockApi : ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}


class FakeApiService : ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(page: Int, size: Int): StoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()

        for (i in 0..100) {
            val quote = ListStoryItem(
                "https://picsum.photos/seed/$i/200/300",
                "0.0",
                "Title $i",
                "Description $i",
                0.0,
                "id $i",
                0.0
            )
            items.add(quote)
        }

        return StoryResponse(items)
    }

    override suspend fun uploadImageWithLocation(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        long: Float
    ): UploadResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody
    ): UploadResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        TODO("Not yet implemented")
    }
}

