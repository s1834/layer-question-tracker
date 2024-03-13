package com.example.questionstracker

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.questionstracker.ui.screens.DateChooseScreen
import com.example.questionstracker.ui.screens.InsertNoOfQuestions
import com.example.questionstracker.ui.screens.QuestionsSolvedCard
import com.example.questionstracker.ui.screens.StatsScreen
import kotlinx.coroutines.launch


enum class AppScreen(@StringRes val title: Int) {
    DateChoose(title = R.string.question_tracker),
    ShowNoOfQuestions(title = R.string.show),
    InsertNoOfQuestions(title = R.string.questions),
    Stats(title = R.string.stats)
}
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun QuestionsTrackerApp(
    viewModel: QuestionTrackerViewModel,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.DateChoose.name
    )
    viewModel.getTotalQuestions()
    Scaffold(
        topBar = {
            QuestionsTrackerAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        }
    ) {innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = AppScreen.DateChoose.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.DateChoose.name) {
                DateChooseScreen(
                    date = uiState.date,
                    showDatePicker = uiState.showDatePicker,
                    totalQuestionsMap = uiState.totalQuestionsMap,
                    onShowButtonClicked = {
                        viewModel.setDate(it)
                        viewModel.getQuestionsSolved(it)
                        navController.navigate(AppScreen.ShowNoOfQuestions.name)
                    },
                    onInsertButtonClicked = {
                        viewModel.setDate(it)
                        viewModel.getQuestionsSolved(it)
                        navController.navigate(AppScreen.InsertNoOfQuestions.name)
                    },
                    onDismiss = {viewModel.setShowDatePicker(it)},
                    onClick = {
                        viewModel.fetchStats()
                        navController.navigate(AppScreen.Stats.name)
                    }
                )
            }
            composable(route = AppScreen.ShowNoOfQuestions.name) {
                QuestionsSolvedCard(uiState.questionsSolved)
            }

            composable(route = AppScreen.InsertNoOfQuestions.name) {
                val coroutineScope = rememberCoroutineScope()
                InsertNoOfQuestions(
                    date = uiState.date,
                    existingData = uiState.questionsSolved,
                    onClick = {noOfLeetcode, noOfCodeforces, noOfCodechef, noOfOthers ->
                        viewModel.setQuestionsSolved(date = uiState.date,
                            noOfLeetcode = noOfLeetcode,
                            noOfCodeforces = noOfCodeforces,
                            noOfCodechef = noOfCodechef,
                            noOfOthers = noOfOthers
                        )
                        viewModel.setInsertingData(true)
                    },
                    onCancelButtonClicked = {navController.navigateUp()}
                )
                if(uiState.isInsertingData) {
                    Log.d("HELLO2", uiState.questionsSolved.noOfLeetcode.toString())
                    coroutineScope.launch {
                        viewModel.upsertQuestionsSolved(uiState.questionsSolved)
                        viewModel.setInsertingData(false)
                        navController.navigateUp()
                    }
                }
            }
            composable(route = AppScreen.Stats.name) {
                val titleList = listOf<String>(
                    "Total Active Days",
                    "Current Streak",
                    "Highest Streak",
                    "Last 30 Days"
                )
                val numberList = listOf<Int>(
                    uiState.totalActiveDays,
                    uiState.streak,
                    uiState.highestStreak,
                    uiState.noOfQuestionsLast30Days
                )
                val imageList = listOf<Int>(
                    R.drawable.active_icon,
                    R.drawable.streak_icon,
                    R.drawable.higheststreak_icon,
                    R.drawable.last_30_days_icon
                )
                StatsScreen(
                    titleList = titleList,
                    dataList = numberList,
                    imageList = imageList
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsTrackerAppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(id = currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
        )
}