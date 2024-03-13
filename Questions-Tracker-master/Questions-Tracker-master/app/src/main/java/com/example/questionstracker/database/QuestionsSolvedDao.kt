package com.example.questionstracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface QuestionsSolvedDao {
    @Upsert
    suspend fun upsertQuestionsSolved(questionsSolved: QuestionsSolved)

    @Query("DELETE FROM QuestionsSolved")
    suspend fun deleteAllQuestionsSolved()

    @Query("SELECT * FROM QuestionsSolved WHERE date = :date")
    fun getQuestionsSolvedByDate(date: String): QuestionsSolved

    @Query("SELECT SUM(noOfLeetcode)+SUM(noOfCodeChef)+SUM(noOfCodeforces)+SUM(others) FROM QuestionsSolved WHERE date > date('now','-30 days')")
    fun getQuestionsSolvedLast30Days(): Int

    @Query("SELECT SUM(noOfLeetcode) FROM QuestionsSolved")
    fun getLeetcodeTotalQuestions(): Int

    @Query("SELECT SUM(noOfCodeforces) FROM QuestionsSolved")
    fun getCodeforcesTotalQuestions(): Int

    @Query("SELECT SUM(noOfCodechef) FROM QuestionsSolved")
    fun getCodechefTotalQuestions(): Int

    @Query("SELECT SUM(others) FROM QuestionsSolved")
    fun getOthersTotalQuestions(): Int

    @Query("SELECT COUNT(*) FROM QuestionsSolved WHERE noOfLeetcode+noOfCodechef+noOfCodeforces+others>0")
    fun getTotalActiveDays(): Int

    @Query("SELECT COUNT(*) as dt FROM (SELECT t1.date, date(t1.date, - (SELECT COUNT(*) FROM QuestionsSolved t2 WHERE t2.date<=t1.date)|| ' day') as grp from QuestionsSolved t1) QuestionsSolved GROUP BY grp")
    fun getHighestStreak(): List<Int>

    @Query("SELECT * FROM QuestionsSolved ORDER BY date DESC")
    fun retrieveAllData(): List<QuestionsSolved>
}