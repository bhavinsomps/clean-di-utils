package com.demo.cleanproject.di

import android.content.Context
import android.content.SharedPreferences
import com.demo.cleanproject.utils.DataStoreHelper
import com.demo.cleanproject.utils.PreferenceHelper
import com.demo.cleanproject.utils.commonUtils.DeviceHelper
import com.demo.cleanproject.utils.commonUtils.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

}