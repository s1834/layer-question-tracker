package com.example.questionstracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionsSolved(
    val noOfLeetcode: Int = 0,
    val noOfCodeforces: Int = 0,
    val noOfCodechef: Int = 0,
    val others: Int = 0,
    @PrimaryKey
    val date: String = "",
)
