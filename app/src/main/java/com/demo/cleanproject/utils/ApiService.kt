package com.demo.cleanproject.utils

import com.demo.cleanproject.model.PostModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): Response<List<PostModel>>

    @GET("posts")
    fun getPostsEnqueue(): Call<ArrayList<PostModel>>

/*    @FormUrlEncoded
    @POST("getspleetresult")
    fun getResultResponse(
        @Field("uuid") responseLength: String
    ): Call<ResultResponse>*/

/*    @Multipart
    @POST("spleetaudio")
    fun uploadAudioFile(
        @Part audio: MultipartBody.Part,
        @Part("stems") stems: RequestBody,
        @Part("device_id") deviceId: RequestBody,
        @Part("is_paid") isPaid: RequestBody
    ): Call<SplitResponse> // Replace with your response class

    @Multipart
    @POST("importMedia")
    fun uploadAudioFileFromUrl(
        @Part("stems") stems: RequestBody,
        @Part("device_id") deviceId: RequestBody,
        @Part("is_paid") isPaid: RequestBody,
        @Part("mediaUrl") mediaUrl: RequestBody
    ): Call<SplitResponse> // Replace with your response class*/
}