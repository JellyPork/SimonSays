package com.changrojasalex1168165.simonsays.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

//Hamburger option that shows the options

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = "Simon Says Game") },
        navigationIcon = {
            IconButton(onClick = { /* Handle navigation icon click */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            // Add action items for other functionalities (if any)
        }
    )
}