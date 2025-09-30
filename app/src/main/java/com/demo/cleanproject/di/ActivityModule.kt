package com.demo.cleanproject.di

import android.content.Context
import com.demo.cleanproject.utils.commonUtils.ImageHelper
import com.demo.cleanproject.utils.commonUtils.NavigationHelper
import com.demo.cleanproject.utils.commonUtils.ToastHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class) // Install in ActivityComponent for Activity-scoped dependencies
object ActivityModule {
    @Provides
    @ActivityScoped
    fun provideActivityContext(@ActivityContext context: Context): Context {
        return context
    }

    @Provides
    @ActivityScoped
    fun providesNavigationHelper(activityContext: Context): NavigationHelper {
        return NavigationHelper(activityContext)
    }

    @Provides
    @ActivityScoped
    fun providesToastHelper(activityContext: Context): ToastHelper {
        return ToastHelper(activityContext)
    }

    @Provides
    @ActivityScoped
    fun providesImageHelper(activityContext: Context): ImageHelper {
        return ImageHelper(activityContext)
    }
}