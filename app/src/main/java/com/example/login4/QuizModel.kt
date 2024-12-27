package com.example.login4

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// The main model for the quiz
data class QuizModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val questionList: List<QuestionModel>
) {
    constructor() : this("", "", "", "", emptyList())
}

// The model for each question in the quiz
data class QuestionModel(
    val question: String,
    val options: List<String>,
    val correct: String
) {
    constructor() : this("", emptyList(), "")
}

// Represents the result of a question (whether answered correctly or not)
@Parcelize
data class QuestionResult(
    val question: String,            // The question text
    val selectedAnswer: String,      // The answer selected by the user
    val correctAnswer: String,       // The correct answer to the question
    val isCorrect: Boolean           // Whether the selected answer is correct
) : Parcelable {
    constructor() : this("", "", "", false) // Default constructor for Firebase
}
