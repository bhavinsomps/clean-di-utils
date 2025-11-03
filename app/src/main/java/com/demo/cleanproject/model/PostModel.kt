package com.demo.cleanproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostModel(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)