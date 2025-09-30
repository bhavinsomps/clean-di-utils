package com.demo.cleanproject.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.demo.cleanproject.R
import com.demo.cleanproject.model.Category
import com.google.gson.Gson
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "lovify_prefs")

@Singleton
class DataStoreHelper @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {

    private object Keys {
        val SHOW_INTRO = booleanPreferencesKey("introShown")
        val PROFILE_IMAGE = intPreferencesKey("selected_profile_image")
        val USER_NAME = stringPreferencesKey("user_name")
        val CUSTOM_CATEGORY = stringPreferencesKey("custom_category")
    }

    // Helper function for saving
    private suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs -> prefs[key] = value }
    }

    // Flow getters
    val profilePic: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.PROFILE_IMAGE] ?: R.drawable.ic_launcher_foreground
    }

    suspend fun setProfilePic(value: Int) = save(Keys.PROFILE_IMAGE, value)

    val userName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_NAME] ?: "New User"
    }

    suspend fun setUserName(value: String) = save(Keys.USER_NAME, value)

    val isIntroShown: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SHOW_INTRO] ?: false
    }

    suspend fun setIntroShown(value: Boolean) = save(Keys.SHOW_INTRO, value)

    val customCategory: Flow<Category> = context.dataStore.data.map { prefs ->
        val json = prefs[Keys.CUSTOM_CATEGORY]
        if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, Category::class.java)
        } else {
            Category("Custom", "ic_custom_category", false, arrayListOf())
        }
    }

    suspend fun setCustomCategory(value: Category) {
        save(Keys.CUSTOM_CATEGORY, Gson().toJson(value))
    }

}
