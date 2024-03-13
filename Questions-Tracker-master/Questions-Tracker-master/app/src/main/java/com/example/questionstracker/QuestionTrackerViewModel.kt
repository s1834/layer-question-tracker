package com.example.questionstracker

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.questionstracker.data.QuestionTrackerUiState
import com.example.questionstracker.database.QuestionsSolved
import com.example.questionstracker.database.QuestionsSolvedDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class QuestionTrackerViewModel(
    private val dao: QuestionsSolvedDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestionTrackerUiState())
    val uiState: StateFlow<QuestionTrackerUiState> = _uiState.asStateFlow()

    fun setDate(date: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = date
            )
        }
    }

    fun setShowDatePicker(isVisible: Boolean) {
        _uiState.update {currentState ->
            currentState.copy(
                showDatePicker = isVisible
            )
        }
    }

    fun setNoOfQuestions(number: Int) {
        _uiState.update {currentState ->
            currentState.copy(
                noOfQuestions = number
            )
        }
    }

    fun getQuestionsSolved(date: String){
        var questionsSolved = dao.getQuestionsSolvedByDate(date)
        if(questionsSolved==null) {
            questionsSolved = QuestionsSolved(date = date)
        }
        _uiState.update {currentState ->
            currentState.copy (
                questionsSolved = questionsSolved
            )
        }
    }


    fun getTotalQuestions() {
        val totalLeetcodeQuestions = dao.getLeetcodeTotalQuestions()
        val totalCodeforcesQuestions = dao.getCodeforcesTotalQuestions()
        val totalCodechefQuestions = dao.getCodechefTotalQuestions()
        val totalOtherQuestions = dao.getOthersTotalQuestions()
        val totalQuestionsMap: Map<String, Int> =  mapOf(
            Pair("Leetcode", totalLeetcodeQuestions),
            Pair("Codeforces", totalCodeforcesQuestions),
            Pair("CodeChef", totalCodechefQuestions),
            Pair("Others", totalOtherQuestions)
        )
        _uiState.update { currentState ->
            currentState.copy(
                totalQuestionsMap = totalQuestionsMap
            )
        }
    }
    fun setQuestionsSolved(
        date: String,
        noOfLeetcode: Int,
        noOfCodeforces: Int,
        noOfCodechef: Int,
        noOfOthers: Int
    ) {
        val tempObj = QuestionsSolved(
            date = date,
            noOfLeetcode = noOfLeetcode,
            noOfCodechef = noOfCodechef,
            noOfCodeforces = noOfCodeforces,
            others = noOfOthers
        )
        _uiState.update {currentState ->
            currentState.copy (
                questionsSolved = tempObj
            )
        }
    }

    fun setInsertingData(isInsertingData: Boolean) {
        _uiState.update { currentState ->
            currentState.copy (
                isInsertingData = isInsertingData
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchStats() {
        var noOfQuestionsLast30Days = dao.getQuestionsSolvedLast30Days()
        if(noOfQuestionsLast30Days==null) {
            noOfQuestionsLast30Days = 0
        }
        var totalActiveDays = dao.getTotalActiveDays()
        if(totalActiveDays==null) {
            totalActiveDays = 0
        }
        val data = dao.retrieveAllData()
        var dates = data.map {
            if(it.noOfCodechef>0 || it.noOfCodeforces>0 || it.noOfLeetcode>0 || it.others>0) {
                it.date
            }
            else {
                "1900-01-01"
            }
        }
        var highestStreak = getHighestStreak(dates)
        dates = dates.sorted().reversed()
        val streak = getCurrentStreak(dates)

        if(streak>highestStreak) {
            highestStreak = streak
        }
        _uiState.update { currentState ->
            currentState.copy (
                noOfQuestionsLast30Days = noOfQuestionsLast30Days,
                totalActiveDays = totalActiveDays,
                highestStreak = highestStreak,
                streak = streak
            )
        }
    }


    suspend fun upsertQuestionsSolved(data: QuestionsSolved) {
        val existingRow = dao.getQuestionsSolvedByDate(data.date)
        if(existingRow==null) {
            dao.upsertQuestionsSolved(data)
        }
        else {
            Log.d("HELLO", existingRow.noOfLeetcode.toString())
            val newObj = QuestionsSolved(
                date = data.date,
                noOfCodechef = data.noOfCodechef+existingRow.noOfCodechef,
                noOfCodeforces = data.noOfCodeforces+existingRow.noOfCodeforces,
                noOfLeetcode = data.noOfLeetcode+existingRow.noOfLeetcode,
                others = data.others+existingRow.others
            )
            dao.upsertQuestionsSolved(newObj)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentStreak(dates: List<String>) : Int {
        var currentStreak = 0
        if(dates.isNotEmpty()) {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val dateObjects = dates.map {formatter.parse(it) }
            if(isToday(dates[0]) || isYesterday(dates[0])) {
                currentStreak = 1
            }
            else {
                return 0;
            }
            for(i in 1 until dateObjects.size) {
                val previousDate = dateObjects[i]
                val currentDate = dateObjects[i-1]
                if (currentDate != null) {
                    if(previousDate?.let { currentDate.isNextDay(it) } == true)  {
                        currentStreak++
                    }
                    else {
                        break
                    }
                }
            }
        }
        return currentStreak
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getHighestStreak(dates: List<String>) : Int {
        var currentStreak = 0
        var highestStreak = 0
        if(dates.isNotEmpty()) {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val dateObjects = dates.map {formatter.parse(it) }
            for(i in 1 until dateObjects.size) {
                val previousDate = dateObjects[i]
                val currentDate = dateObjects[i-1]
                if (currentDate != null) {
                    if(previousDate?.let { currentDate.isNextDay(it) } == true)  {
                        currentStreak++
                    }
                    else {
                        highestStreak = max(currentStreak, highestStreak)
                        currentStreak = 0
                    }
                }
            }
        }
        return highestStreak
    }

    private fun Date.isNextDay(otherDate: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = otherDate
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val currDate = Date(this.time)
        val prevDate = Date(calendar.timeInMillis)
        Log.d("HELLO", currDate.toString())
        Log.d("HELLO2", prevDate.toString())
        return currDate==prevDate
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun isToday(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Adjust format if needed
        val parsedDate = LocalDate.parse(dateString, formatter)
        val today = LocalDate.now()
        return parsedDate == today
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isYesterday(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Adjust format if needed
        val parsedDate = LocalDate.parse(dateString, formatter)
        val yesterday = LocalDate.now().minusDays(1)
        return parsedDate == yesterday
    }
}

