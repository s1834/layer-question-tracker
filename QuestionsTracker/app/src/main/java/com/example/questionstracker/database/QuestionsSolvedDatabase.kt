package com.example.questionstracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [QuestionsSolved::class],
    version = 2
)
abstract class QuestionsSolvedDatabase: RoomDatabase() {
    abstract val dao: QuestionsSolvedDao
}