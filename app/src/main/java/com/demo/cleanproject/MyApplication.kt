package com.demo.cleanproject

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    lateinit var mInstance: MyApplication
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        context = applicationContext
    }

}
