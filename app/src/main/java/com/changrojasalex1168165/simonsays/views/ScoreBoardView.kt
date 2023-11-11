package com.changrojasalex1168165.simonsays.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.changrojasalex1168165.simonsays.dto.GameResult
import com.changrojasalex1168165.simonsays.database.GameViewModel

@Composable
fun ScoreBoardComponent(navController: NavHostController, viewModel: GameViewModel) {
    val gameResults by viewModel.gameResults.collectAsState(initial = emptyList())

    LazyColumn {
        items(gameResults) { result ->
            GameResultCard(result = result)
        }
    }
}

@Composable
fun GameResultCard(result: GameResult) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                Text(
                    text = "${result.dateTime}",
                    style = TextStyle(fontFamily = FontFamily.Monospace)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Rounds: ${result.round}")
            }

            Spacer(modifier = Modifier.height(8.dp)) // Adding space between rows

            Row {
                Text(
                    text = " ${result.playerName}",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Time Elapsed: ${result.timeElapsed}",
                    style = TextStyle(fontFamily = FontFamily.Monospace)
                )
            }
        }
    }
}
