package com.demo.cleanproject.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.cleanproject.model.PostModel

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getAllPosts(): LiveData<List<PostModel>>

    @Query("SELECT * FROM posts")
    suspend fun getAllPostsList(): List<PostModel>  // for manual fetching

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostModel>)
}