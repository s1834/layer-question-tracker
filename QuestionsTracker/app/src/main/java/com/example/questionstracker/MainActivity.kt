package com.example.questionstracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.questionstracker.database.QuestionsSolvedDatabase
import com.example.questionstracker.ui.screens.StatsCard
import com.example.questionstracker.ui.screens.StatsScreen
import com.example.questionstracker.ui.theme.QuestionsTrackerTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            QuestionsSolvedDatabase::class.java,
            "questionssolved.db"
        )
            //.createFromAsset("database/QuestionsSolved.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    private val viewModel by viewModels<QuestionTrackerViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return QuestionTrackerViewModel(db.dao) as T
                }
            }
        }
    )
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuestionsTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuestionsTrackerApp(viewModel = viewModel)

                }
            }
        }
    }
}
