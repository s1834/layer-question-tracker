package com.example.questionstracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.questionstracker.R
import com.example.questionstracker.database.QuestionsSolved

@Composable
fun InsertNoOfQuestions(
    date: String,
    existingData: QuestionsSolved,
    onClick: (Int, Int, Int, Int) -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var noOfLeetcode by remember { mutableStateOf("") }
    var noOfCodeforces by remember { mutableStateOf("") }
    var noOfCodechef by remember { mutableStateOf("") }
    var noOfOthers by remember { mutableStateOf("") }
    var isSubmitEnabled by remember { mutableStateOf(false) }

    isSubmitEnabled = ((noOfCodechef != "") || (noOfLeetcode != "") || (noOfCodeforces != "") || (noOfOthers != "" )) &&
            ((noOfCodechef != "0") || (noOfLeetcode != "0") || (noOfCodeforces != "0" || (noOfOthers != "0")))

    if(noOfCodeforces!="") {
        for(i in 0 until noOfCodeforces.length) {
            if(!noOfCodeforces[i].isDigit()) {
                if(noOfCodeforces.length>1 && i==0 && noOfCodeforces[i]=='-') {
                    isSubmitEnabled = true;
                    continue;
                }
                isSubmitEnabled = false
            }
        }
    }
    if(noOfCodechef!="") {
        for(i in 0 until noOfCodechef.length) {
            if(!noOfCodechef[i].isDigit()) {
                if(noOfCodechef.length>1 && i==0 && noOfCodechef[i]=='-') {
                    isSubmitEnabled = true;
                    continue;
                }
                isSubmitEnabled = false
            }
        }
    }
    if(noOfLeetcode!="") {
        for(i in 0 until noOfLeetcode.length) {
            if(!noOfLeetcode[i].isDigit()) {
                if(noOfLeetcode.length>1 && i==0 && noOfLeetcode[i]=='-') {
                    isSubmitEnabled = true;
                    continue;
                }
                isSubmitEnabled = false
            }
        }
    }

    if(noOfOthers!="") {
        for(i in 0 until noOfOthers.length) {
            if(!noOfOthers[i].isDigit()) {
                if(noOfOthers.length>1 && i==0 && noOfOthers[i]=='-') {
                    isSubmitEnabled = true;
                    continue;
                }
                isSubmitEnabled = false
            }
        }
    }

    if(isSubmitEnabled) {
        if(noOfLeetcode!="") {
            val temp = noOfLeetcode.toInt()
            if(temp<0) {
                if(temp+existingData.noOfLeetcode<0) {
                    isSubmitEnabled = false
                }
            }
        }
        if(noOfCodeforces!="") {
            val temp = noOfCodeforces.toInt()
            if(temp<0) {
                if(temp+existingData.noOfCodeforces<0) {
                    isSubmitEnabled = false
                }
            }
        }
        if(noOfCodechef!="") {
            val temp = noOfCodechef.toInt()
            if(temp<0) {
                if(temp+existingData.noOfCodechef<0) {
                    isSubmitEnabled = false
                }
            }
        }
        if(noOfOthers!="") {
            val temp = noOfOthers.toInt()
            if(temp<0) {
                if(temp+existingData.others<0) {
                    isSubmitEnabled = false
                }
            }
        }
    }



    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val newDate = "${date.subSequence(8, 10)}-${date.subSequence(5,7)}-${date.subSequence(0,4)}"
        OutlinedTextField(
            value = newDate + ": Existing Data",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            value = noOfLeetcode,
            label = { Text(text = stringResource(R.string.leetcode) + ": ${existingData.noOfLeetcode}") },
            onValueChange = {
                noOfLeetcode = it
            },
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.leetcode_icon),
                    contentDescription = stringResource(id = R.string.no_of_leetcode_questions)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = noOfCodeforces,
            label = { Text(text = stringResource(R.string.codeforces) + ": ${existingData.noOfCodeforces}") },
            onValueChange = {
                noOfCodeforces = it
            },
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.codeforces_icon),
                    contentDescription = stringResource(id = R.string.no_of_codeforces_questions)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = noOfCodechef,
            label = { Text(text = stringResource(R.string.codechef) + ": ${existingData.noOfCodechef}") },
            onValueChange = {
                noOfCodechef = it
            },
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.codechef_icon),
                    contentDescription = stringResource(id = R.string.no_of_codechef_questions)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = noOfOthers,
            label = { Text(text = stringResource(R.string.others) + ": ${existingData.others}") },
            onValueChange = {
                noOfOthers = it
            },
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.other_icon),
                    contentDescription = stringResource(id = R.string.no_of_other_questions),
                    modifier = Modifier.size(24.dp)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        
        Text(
            text = "Note: The data will be added to the existing data",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = "Negative numbers will be subtracted from the existing data",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if(noOfLeetcode=="") {
                        noOfLeetcode = "0"
                    }
                    if(noOfCodechef=="") {
                        noOfCodechef = "0"
                    }
                    if(noOfCodeforces=="") {
                        noOfCodeforces = "0"
                    }
                    if(noOfOthers=="") {
                        noOfOthers = "0"
                    }
                    onClick(noOfLeetcode.toInt(), noOfCodeforces.toInt(), noOfCodechef.toInt(), noOfOthers.toInt())
                },
                enabled = isSubmitEnabled,
                modifier = Modifier.padding(4.dp)
            )
            {
                Text(text = stringResource(R.string.done))
            }

            Button(
                onClick = onCancelButtonClicked,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewScreen() {
    //insertNoOfQuestions(date = "22/02/2024")
}