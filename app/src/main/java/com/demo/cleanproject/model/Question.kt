package com.demo.cleanproject.model

data class Question(
    val question: String = "",
    val options: ArrayList<String> = arrayListOf(),
)