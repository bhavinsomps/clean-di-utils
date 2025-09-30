package com.demo.cleanproject.utils.commonUtils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceHelper @Inject constructor(@param:ApplicationContext private val context: Context) {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getPackageInfo(): PackageInfo? {
        val manager = context.packageManager
        var info: PackageInfo? = null
        try {
            info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return info
    }
}