package com.example.questionstracker.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.questionstracker.R
import com.example.questionstracker.database.QuestionsSolved

@Composable
fun QuestionsSolvedCard(
    questionObj: QuestionsSolved,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .verticalScroll(rememberScrollState())
    ){
        val date = questionObj.date
        val newDate = "${date.subSequence(8, 10)}-${date.subSequence(5,7)}-${date.subSequence(0,4)}"
        Text(
            text = newDate,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 8.dp, top = 16.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        NoOfQuestionsCard(
            text = "Leetcode",
            noOfQuestions = questionObj.noOfLeetcode,
            image = R.drawable.leetcode_logo
        )
        Spacer(modifier = Modifier.padding(8.dp))
        NoOfQuestionsCard(
            text = "Codeforces",
            noOfQuestions = questionObj.noOfCodeforces,
            image = R.drawable.codeforces_logo
        )
        Spacer(modifier = Modifier.padding(8.dp))
        NoOfQuestionsCard(
            text = "Codechef",
            noOfQuestions = questionObj.noOfCodechef,
            image = R.drawable.codechef_logo
        )

        Spacer(modifier = Modifier.padding(8.dp))

        NoOfQuestionsCard(
            text = "Other",
            noOfQuestions = questionObj.others,
            image = R.drawable.other_logo
        )
    }

}


@Composable
fun NoOfQuestionsCard(
    text: String,
    noOfQuestions: Int,
    @DrawableRes image: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Text(
                    text = noOfQuestions.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }

            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                alpha = 0.7f,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(100.dp)
                    .padding(end = 8.dp)
            )
        }

    }
}
@Composable
@Preview(showSystemUi = true)
fun CardPreview() {
//    var tempObj = QuestionsSolved(date = "02/02/2024", noOfLeetcode = 7, noOfCodeforces = 3, noOfCodechef = 2)
//    QuestionsSolvedCard(questionObj = tempObj)
    //NoOfQuestionsCard(text = "Leetcode", noOfQuestions = 5)
}