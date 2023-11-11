package com.changrojasalex1168165.simonsays.views

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.changrojasalex1168165.simonsays.database.GameViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainMenuComponent(navController: NavHostController, viewModel: GameViewModel) {
    val options = listOf("4 Buttons", "6 Buttons")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    val context = LocalContext.current
    //Remembering name
    var name by remember { mutableStateOf("") }

    //Game Variables
    var resetGame by remember { mutableStateOf(true) }


    //BottomSheet Variables
    var openBottomSheet by remember { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        //Functioning
        Text(text = "Simon Says", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // "Play" button to show the bottom sheet
        Button(onClick = {
            openBottomSheet = true
        }) {
            Text(text = "Play")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // "Top Scoreboards" button to navigate to the Scoreboards activity
        Button(onClick = {
            // Inside your Composable function
            val gameResults = viewModel.getGameResults().value
            Log.d("Tag for Game Results", gameResults.toString())
            
            navController.navigate("scoreboards")
            // Use the appropriate destination in your navigation graph
        }) {
            Text(text = "Top Scoreboards")
        }



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
                    Text(text = "Choose the number of buttons:")
                    Spacer(modifier = Modifier.height(8.dp))
                    // RadioGroup for button options
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    ) {
                        TextField(
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            value = selectedOptionText,
                            onValueChange = {},
                            label = { Text("Label") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            options.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        selectedOptionText = selectionOption
                                        expanded = false
                                        Log.d("Selected Option value", selectedOptionText)
                                    },
                                )
                            }
                        }
                    }

                    Text(text = "Enter your name:")
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Enter Nickname") },
                        modifier = Modifier.fillMaxWidth()
                    )



                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to send the name to the other page
                    Button(onClick = {
                        // Reset the game
                        resetGame = true
                        openBottomSheet = false

                        val selectedValue = if (selectedOptionText == "4 Buttons") 4 else 6

                        navController.navigate("game/$selectedValue/$name")

                        Log.d("Button Clicked", "Button for MainMenu was clicked and sent name to gameplay")
                        Log.d("Button Clicked Value Option", selectedValue.toString())
                    }) {
                        Text(text = "Play")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button to go back
                    Button(onClick = {
                        openBottomSheet = false
                    }) {
                        Text(text = "Go Back")
                    }
                }
            }
        }
    }

}
