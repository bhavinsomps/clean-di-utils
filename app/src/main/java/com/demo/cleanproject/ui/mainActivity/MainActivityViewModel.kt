package com.demo.cleanproject.ui.mainActivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.cleanproject.model.PostModel
import com.demo.cleanproject.utils.ApiService
import com.demo.cleanproject.utils.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    val postList = MutableLiveData<ArrayList<PostModel>>()
    fun loadPosts() {
        viewModelScope.launch {
            try {
                val response = apiService.getPosts()
                if (response.isSuccessful) {
                    val list = ArrayList(response.body() ?: emptyList())
                    postList.postValue(list)
                } else {
                    Log.d("TAGPosts", "API Error: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchPosts() {
        val call = RetrofitClient.apiService.getPostsEnqueue()
        call.enqueue(object : Callback<ArrayList<PostModel>> {
            override fun onResponse(
                call: Call<ArrayList<PostModel>>,
                response: Response<ArrayList<PostModel>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    postList.value?.clear()
                    postList.value?.addAll(response.body()!!)
                    Log.d("API_SUCCESS", "Total Posts: ${postList.value?.size}")
                } else {
                    Log.e("API_ERROR", "Response Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<PostModel>>, t: Throwable) {
                Log.e("API_FAILURE", "Failed: ${t.message}")
            }
        })
    }

}

