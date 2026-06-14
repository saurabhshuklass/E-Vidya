package com.example.e_vidya

data class Question(

    val question: String = "",

    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
    val option4: String = "",

    val correctAnswer: Int = 0,

    var selectedAnswer: Int = 0
)