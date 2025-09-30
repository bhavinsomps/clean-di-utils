package com.demo.cleanproject.model

data class Category(
    var categoryName: String,
    val categoryIcon: String,
    var isSelected: Boolean = false,
    val questions: ArrayList<Question>,
)
