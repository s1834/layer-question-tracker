package com.example.questionstracker.data

import com.example.questionstracker.database.QuestionsSolved


data class QuestionTrackerUiState(
    val date: String = "",
    val showDatePicker: Boolean = false,
    val noOfQuestions: Int = 0,
    val totalQuestionsMap: Map<String, Int> = emptyMap<String, Int>(),
    val isInsertingData: Boolean = false,
    val noOfQuestionsLast30Days: Int = 0,
    val totalActiveDays: Int = 0,
    val highestStreak: Int = 0,
    val streak: Int = 0,
    val questionsSolved: QuestionsSolved = QuestionsSolved()
)
