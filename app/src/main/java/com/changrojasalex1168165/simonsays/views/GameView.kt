package com.changrojasalex1168165.simonsays.views

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.changrojasalex1168165.simonsays.dto.GameResult
import com.changrojasalex1168165.simonsays.database.GameViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun MainGameComponent(
    navController: NavHostController,
    viewModel: GameViewModel,
    int: Int?,
    string: String?
) {


    val buttonsCount = int
    val playerName = string



    val context = LocalContext.current

    // Game UI and Logic Variables
    val buttonColors = generateButtonColors(buttonsCount!!).toMutableList()
    var currentSequence by remember { mutableStateOf(generateRandomButton(buttonsCount!!, 1)) }
    Log.d("Current Sequence", currentSequence.toString())
    var playerInput by remember { mutableStateOf(emptyList<Int>()) }
    var currentRound by remember { mutableStateOf(1) }
    val rows = if (buttonsCount == 4) 2 else 3

    var gameStopped by remember {
        mutableStateOf(false)
    }
    var gameWon by remember {
        mutableStateOf(false)
    }
    var sequencePlaying by remember {
        mutableStateOf(false)
    }

    // Timer variables
    var startTime by remember { mutableStateOf(0L) }
    var timerRunning by remember { mutableStateOf(false) }
    var roundStartTime by remember { mutableStateOf(0L) }
    val roundTimeLimit = 10 // Time limit for the player to complete a round (in seconds)
    val idleTimeLimit = 3 // Time limit for player inactivity at the start of a round (in seconds)
    var roundTimeLimitText by remember { mutableStateOf(roundTimeLimit) }

    // Timer to calculate full elapsed time
    var totalElapsedTime by remember { mutableStateOf(0L) }

    var timerRunningInternal by remember { mutableStateOf(false) }

    // Function to start/stop the timer
    fun startTimer() {
        timerRunningInternal = true
    }

    fun stopTimer() {
        timerRunningInternal = false
    }

    // Function to reset the timer
    fun resetTimer() {
        startTime = System.currentTimeMillis()
        roundStartTime = startTime
        timerRunning = true
    }

    // Inside MainGameComponent
    fun resetGame() {
        currentSequence = generateRandomButton(buttonsCount, 1)
        playerInput = emptyList()
        currentRound = 1
        gameStopped = false
        gameWon = false

        totalElapsedTime = 0L
        roundTimeLimitText = roundTimeLimit
        resetTimer()


        // Start the game activity anew
        navController.navigate("game/$buttonsCount/$playerName")
    }

    fun returnToMenu() {
        // Return to the main activity
        navController.navigate("menu")
    }






    var timerJob: Job? by remember { mutableStateOf(null) }

    // Timer display
    LaunchedEffect(timerRunningInternal) {
        timerJob?.cancel()
        if (timerRunningInternal) {
            timerJob = launch {
                while (timerRunningInternal) {
                    val currentTime = System.currentTimeMillis()
                    val elapsedRoundTime = (currentTime - roundStartTime) / 1000

                    if (elapsedRoundTime >= idleTimeLimit) {
                        if (playerInput.isEmpty()) {
                            if (elapsedRoundTime >= idleTimeLimit + roundTimeLimit) {
                                Toast.makeText(context, "Sorry, $playerName! You lost the round.", Toast.LENGTH_LONG).show()
                                gameStopped = true
                                gameWon = false
                                stopTimer() // Stop the timer internally
                            } else {
                                roundTimeLimitText = (roundTimeLimit - (elapsedRoundTime - idleTimeLimit)).toInt()
                            }
                        } else {
                            roundStartTime = currentTime
                        }
                    }

                    totalElapsedTime += 1

                    delay(1000)
                }
            }
        }
    }


    val animationSequenceActive = remember { mutableStateListOf<Boolean>() }

    for (i in 0 until buttonsCount) {
        animationSequenceActive.add(false)
    }

    val alphaAnimations = List(buttonsCount) { remember { Animatable(1f) } }

    LaunchedEffect(currentSequence.joinToString("_")) {
        sequencePlaying = true

        for (i in currentSequence) {
            alphaAnimations[i].animateTo(0f) // Decrease alpha value for the button at the index i
            delay(500) // Adjust the delay between each button's animation (0.5 seconds)
            alphaAnimations[i].animateTo(1f) // Return alpha value to normal
            delay(500) // Adjust the delay before moving to the next button
        }
        sequencePlaying = false
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp), // Adjust the corner radius as needed
            color = Color.Black, // Change the background color as needed
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), // Adjust padding as needed
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Round: $currentRound", fontSize = 16.sp, color = Color.White) // Adjust text color as needed
                Text(text = "Time: ${formatElapsedTime(totalElapsedTime)}", fontSize = 16.sp, color = Color.White) // Adjust text color as needed
                Text(text = "TimeLimit: $roundTimeLimitText s", fontSize = 16.sp, color = Color.White) // Adjust text color as needed
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(text = "$playSequenceText", fontSize = 32.sp) // Adjust text color as needed

        Spacer(modifier = Modifier.height(16.dp))
        // Display the buttons
        for (row in 0 until rows) {

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0 until buttonsCount / rows) {
                    val index = row * (buttonsCount / rows) + col


                    Button(
                        onClick = {
                            if(!sequencePlaying){
                                // Handle button click
                                startTimer()

                                playerInput += index
                                Log.d("Player Input Sequence", playerInput.toString())
                                Log.d("Current Sequence", currentSequence.toString())

                                if (playerInput.size == currentRound) {
                                    // Check if the player's input matches the current sequence
                                    if (playerInput == currentSequence) {
                                        currentSequence = if (playerInput.size < currentRound) {
                                            // Player entered the correct sequence, add a new random button to the existing sequence
                                            val newRandomButton = Random.nextInt(0, buttonsCount)
                                            currentSequence + newRandomButton
                                        } else {
                                            // Player completed the round, generate a new random sequence with the same length
                                            currentRound++

                                            val newRandomButton = Random.nextInt(0, buttonsCount)
                                            currentSequence + newRandomButton


                                        }

                                        playerInput = emptyList()

                                        if (currentRound == 8) {
                                            // Player won the game
                                            timerRunning = false
                                            stopTimer()
                                            Toast.makeText(
                                                context,
                                                "Congratulations, $playerName! You won!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            gameStopped = true
                                            gameWon = true
                                        }
                                    } else {
                                        // Player made a mistake, handle game over

                                        stopTimer()
                                        Toast.makeText(
                                            context,
                                            "$playerName! You lost.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        gameStopped = true
                                        gameWon = false
                                        timerRunning = false
                                        roundTimeLimitText =
                                            roundTimeLimit // Reset the round time limit
                                        resetTimer()
                                        // Return to MainActivity
                                        // ...
                                    }
                                    roundTimeLimitText =
                                        roundTimeLimit // Reset the round time limit
                                    resetTimer() // Reset the timer for the next round

                                }
                            }

                        },
                        modifier = Modifier
                            .size(100.dp)
                            .alpha(alphaAnimations[index].value),
                        colors = buttonColors[index],

                        ) {
                        // Button content
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }



    if (gameStopped) {
        val gameOutcomeText = if (gameWon) "Congratulations, $playerName! You won!" else "$playerName! You lost."
        val timeElapsedText = "Time: ${formatElapsedTime(totalElapsedTime)}"

        if(gameWon){
            val recordGameResult = GameResult(
                playerName = playerName.toString(),
                round = currentRound,
                timeElapsed = formatElapsedTime(totalElapsedTime),
                dateTime = formatCurrentDate()
            )

            viewModel.addGameResult(recordGameResult)
        }

        BottomSheetModified(
            openBottomSheet = true,
            textShown = gameOutcomeText,
            totalTimeElapsed = timeElapsedText,
            context = context,
            onNewGameClick = {
                timerRunning = false // Stop the timer
                resetGame()
            }
        ) {
            timerRunning = false // Stop the timer
            returnToMenu()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetModified(
    openBottomSheet: Boolean,
    textShown: String,
    context: Context,
    totalTimeElapsed: String,
    onNewGameClick: () -> Unit,
    onReturnToMenuClick: () -> Unit
) {
    //BottomSheet Variables
    var openBottomSheet by remember { mutableStateOf(openBottomSheet) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    // Bottom Sheet Container
    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = { openBottomSheet = true },
            sheetState = bottomSheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = textShown)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Time played: $totalTimeElapsed")
                Spacer(modifier = Modifier.height(16.dp))

                // Button to start a new game
                Button(onClick = onNewGameClick) {
                    Text(text = "New Game")
                }

                // Button to return to the main activity
                Button(onClick = onReturnToMenuClick) {
                    Text(text = "Return to Menu")
                }

                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }
}



fun playingTextChange(sequencePlaying: Boolean): String {
    var string: String
    if (sequencePlaying){
        string = "Wait"
    }else{
        string = "Play"
    }

    return string
}

// Helper function to format the elapsed time
fun formatElapsedTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, remainingSeconds)
}

fun formatCurrentDate(): String {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy - hh:mma", Locale.getDefault())
    return dateFormat.format(Date())
}



fun generateRandomButton(count: Int, round: Int): List<Int> {
    return List(round) { Random.nextInt(0, count) }
}


@Composable
fun generateButtonColors(count: Int): List<ButtonColors> {
    val distinctColors = listOf(
        Color(0xFFFF0000), // Red
        Color(0xFF1E7FB5), // Blue
        Color(0xFFFFE500), // Yellow
        Color(0xFF05FF00), // Green
        Color(0xFFEB00FF), // Purple
        Color(0xFF5200FF)  // Indigo
    )

    return List(count) {
        ButtonDefaults.buttonColors(
            containerColor = distinctColors[it],
            contentColor = Color.White
        )
    }
}
