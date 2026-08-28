@file:Suppress("SpellCheckingInspection")

package com.example.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unscramble.ui.theme.UnscrambleTheme

// Vincent Olpindo & Aaron Earl Galutan & Charlee Limayo

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            UnscrambleTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    GameScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) {

    // Words used in the game
    val words = listOf(
        "CAT",
        "DOG",
        "BOOK"
    )

    // Current word
    var currentWordIndex by remember {
        mutableIntStateOf(0)
    }

    // User's answer
    var userAnswer by remember {
        mutableStateOf("")
    }

    // Player score
    var score by remember {
        mutableIntStateOf(0)
    }

    // Current correct answer
    val correctAnswer = words[currentWordIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Unscramble the Word",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = correctAnswer,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(20.dp)
        )

        OutlinedTextField(
            value = userAnswer,

            onValueChange = {
                userAnswer = it
            },

            label = {
                Text("Enter your answer")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

                if (userAnswer.uppercase() == correctAnswer) {

                    score++

                    if (currentWordIndex < words.lastIndex) {

                        currentWordIndex++

                        userAnswer = ""
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {

            Text(
                text = "SUBMIT",
                fontSize = 16.sp
            )
        }

        Text(
            text = "Score: $score",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {

    UnscrambleTheme {
        GameScreen()
    }
}