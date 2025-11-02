package com.demo.cleanproject.di

import android.content.Context
import android.content.SharedPreferences
import com.demo.cleanproject.utils.ApiService
import com.demo.cleanproject.utils.Constants
import com.demo.cleanproject.utils.DataStoreHelper
import com.demo.cleanproject.utils.PreferenceHelper
import com.demo.cleanproject.utils.commonUtils.DeviceHelper
import com.demo.cleanproject.utils.commonUtils.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences{
        return context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferenceHelper(sharedPreferences: SharedPreferences): PreferenceHelper {
        return PreferenceHelper( sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideDataStoreHelper(@ApplicationContext context: Context): DataStoreHelper {
        return DataStoreHelper(context)
    }

    @Provides
    @Singleton
    fun provideDeviceHelper(@ApplicationContext context: Context): DeviceHelper {
        return DeviceHelper(context)
    }

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
        return NetworkHelper(context)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Use BODY for debugging, change to NONE for release
        }
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(preferenceHelper: PreferenceHelper): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val newRequest = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                // Example: add token from shared preferences
                //.header("Authorization", "Bearer ${preferenceHelper.getToken()}")
                .build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // 🔁 Change this to your base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}