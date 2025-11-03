package com.demo.cleanproject.ui.mainActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.cleanproject.model.PostModel
import com.demo.cleanproject.room.PostDao
import com.demo.cleanproject.utils.ApiService
import com.demo.cleanproject.utils.PreferenceHelper
import com.demo.cleanproject.utils.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {
   // val postList = MutableLiveData<ArrayList<PostModel>>()
    private val _postList = MutableLiveData<List<PostModel>>()
    val postList: LiveData<List<PostModel>> get() = _postList

    fun loadPosts() {
        viewModelScope.launch {
            try {
                if (!preferenceHelper.isPostFetched) {
                    // Fetch from API only once
                    val response = apiService.getPosts()
                    if (response.isSuccessful && response.body() != null) {
                        val posts = response.body()!!
                        postDao.insertPosts(posts)
                        preferenceHelper.isPostFetched = true
                        _postList.postValue(posts)
                        Log.d("TAGPosts", "Fetched ${posts.size} posts from API and saved to DB")
                    } else {
                        Log.e("TAGPosts", "API Error: ${response.code()} - ${response.message()}")
                        // fallback to DB if available
                        loadFromDb()
                    }
                } else {
                    // Already fetched, load from DB
                    loadFromDb()
                }
            } catch (e: Exception) {
                Log.e("TAGPosts", "Exception: ${e.message}", e)
                // fallback to DB on failure
                loadFromDb()
            }
        }
    }

    private fun loadFromDb() {
        viewModelScope.launch {
            val localPosts = postDao.getAllPostsList() // Use a suspend fun version, not LiveData
            _postList.postValue(localPosts)
            Log.d("TAGPosts", "Loaded ${localPosts.size} posts from DB")
        }
    }

//    fun fetchPosts() {
//        val call = RetrofitClient.apiService.getPostsEnqueue()
//        call.enqueue(object : Callback<ArrayList<PostModel>> {
//            override fun onResponse(
//                call: Call<ArrayList<PostModel>>,
//                response: Response<ArrayList<PostModel>>
//            ) {
//                if (response.isSuccessful && response.body() != null) {
//                    postList.value?.clear()
//                    postList.value?.addAll(response.body()!!)
//                    Log.d("API_SUCCESS", "Total Posts: ${postList.value?.size}")
//                } else {
//                    Log.e("API_ERROR", "Response Error: ${response.code()} ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<PostModel>>, t: Throwable) {
//                Log.e("API_FAILURE", "Failed: ${t.message}")
//            }
//        })
//    }

}

