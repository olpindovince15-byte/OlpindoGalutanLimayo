@file:Suppress("SpellCheckingInspection")

package com.example.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble.ui.GameViewModel
import com.example.unscramble.ui.theme.UnscrambleTheme

// Vincent Olpindo & Aaron Earl Galutan & Charlee Limayo

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            UnscrambleTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GameStatus(score: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Text(text = stringResource(R.string.score, score),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun GameLayout(
    onUserGuessChanged: (String) -> Unit,
    userGuess: String,
    onKeyboardDone: () -> Unit,
    currentScrambledWord: String,
    isGuessWrong: Boolean,
    wordCount: Int,
    modifier: Modifier = Modifier)
{
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            modifier =
                Modifier.padding(mediumPadding)
        ) {

            // Word count
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        MaterialTheme.colorScheme.surfaceTint
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 4.dp
                    )
                    .align(Alignment.End),

                text = stringResource(
                    R.string.word_count,
                    wordCount
                ),

                style =
                    MaterialTheme.typography.titleMedium,

                color =
                    MaterialTheme.colorScheme.onPrimary
            )

            // Scrambled word
            Text(
                text = currentScrambledWord,
                style =
                    MaterialTheme.typography.displayMedium
            )

            // Instructions
            Text(
                text =
                    stringResource(R.string.instructions),

                textAlign =
                    TextAlign.Center,

                style =
                    MaterialTheme.typography.titleMedium
            )

            // User answer
            OutlinedTextField(
                value = userGuess,

                onValueChange =
                    onUserGuessChanged,

                singleLine = true,

                shape =
                    MaterialTheme.shapes.large,

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        focusedContainerColor =
                            MaterialTheme.colorScheme.surface,

                        unfocusedContainerColor =
                            MaterialTheme.colorScheme.surface,

                        disabledContainerColor =
                            MaterialTheme.colorScheme.surface
                    ),

                label = {
                    Text(
                        stringResource(
                            if (isGuessWrong)
                                R.string.wrong_guess
                            else
                                R.string.enter_your_word
                        )
                    )
                },

                isError =
                    isGuessWrong,

                keyboardOptions =
                    KeyboardOptions(
                        imeAction =
                            ImeAction.Done
                    ),

                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            onKeyboardDone()
                        }
                    )
            )
        }
    }
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {

    // Observe GameViewModel state
    val gameUiState by
    gameViewModel.uiState.collectAsState()

    val mediumPadding =
        dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = modifier
            .verticalScroll(
                rememberScrollState()
            )
            .padding(mediumPadding),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        // App title
        Text(
            text =
                stringResource(R.string.app_name),

            style =
                MaterialTheme.typography.titleLarge
        )

        // Game layout
        GameLayout(

            onUserGuessChanged = {
                gameViewModel.updateUserGuess(it)
            },

            // FIXED
            userGuess =
                gameUiState.userAnswer,

            onKeyboardDone = {
                gameViewModel.checkUserGuess()
            },

            // FIXED
            currentScrambledWord =
                gameUiState.scrambledWord,

            isGuessWrong =
                gameUiState.isGuessedWordWrong,

            wordCount =
                gameUiState.currentWordCount,
 
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding)
        )

        // Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),

            verticalArrangement =
                Arrangement.spacedBy(mediumPadding),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // Submit button
            Button(
                onClick = {
                    gameViewModel.checkUserGuess()
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        stringResource(R.string.submit),

                    fontSize =
                        16.sp
                )
            }

            // Skip button
            OutlinedButton(
                onClick = {
                    gameViewModel.skipWord()
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        stringResource(R.string.skip),

                    fontSize =
                        16.sp
                )
            }
        }

        // Score
        GameStatus(
            score =
                gameUiState.score,

            modifier =
                Modifier.padding(20.dp)
        )

        // Game over dialog
        if (gameUiState.isGameOver) {

            FinalScoreDialog(
                score =
                    gameUiState.score,

                onPlayAgain = {
                    gameViewModel.resetGame()
                }
            )
        }
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {

    val activity =
        LocalActivity.current

    AlertDialog(

        onDismissRequest = {},

        title = {
            Text(
                text =
                    stringResource(
                        R.string.congratulations
                    )
            )
        },

        text = {
            Text(
                text =
                    stringResource(
                        R.string.you_scored,
                        score
                    )
            )
        },

        modifier =
            modifier,

        // Exit button
        dismissButton = {

            TextButton(
                onClick = {
                    activity?.finish()
                }
            ) {

                Text(
                    text =
                        stringResource(
                            R.string.exit
                        )
                )
            }
        },

        // Play Again button
        confirmButton = {

            TextButton(
                onClick =
                    onPlayAgain
            ) {

                Text(
                    text =
                        stringResource(
                            R.string.play_again
                        )
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {

    UnscrambleTheme {
        GameScreen()
    }
}

