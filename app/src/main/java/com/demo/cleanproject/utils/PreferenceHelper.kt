package com.demo.cleanproject.utils

import android.content.SharedPreferences
import com.demo.cleanproject.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper @Inject constructor(private val pref: SharedPreferences) {
    private object Keys {
        const val SHOW_INTRO = "introShown"
        const val PROFILE_IMAGE = "selected_profile_image"
        const val USER_NAME = "user_name"
        const val CUSTOM_CATEGORY = "custom_category"
    }

    private fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var profilePic: Int
        get() = pref.getInt(Keys.PROFILE_IMAGE, 1)
        set(value) = pref.edit { it.putInt(Keys.PROFILE_IMAGE, value) }

    var userName: String
        get() = pref.getString(Keys.USER_NAME, "New User").toString()
        set(value) = pref.edit { it.putString(Keys.USER_NAME, value) }

    var isIntroShown: Boolean
        get() = pref.getBoolean(Keys.SHOW_INTRO, false)
        set(value) = pref.edit { it.putBoolean(Keys.SHOW_INTRO, value) }

    var customCategory: Category
        get() {
            val json = pref.getString(Keys.CUSTOM_CATEGORY, null)
            return if (json != null) {
                val type = object : TypeToken<Category>() {}.type
                Gson().fromJson(json, type)
            } else {
                Category(
                    categoryName = "Custom",
                    categoryIcon = "ic_custom_category",
                    questions = arrayListOf()
                )
            }
        }
        set(value) {
            val json = Gson().toJson(value)
            pref.edit { it.putString(Keys.CUSTOM_CATEGORY, json) }
        }
}