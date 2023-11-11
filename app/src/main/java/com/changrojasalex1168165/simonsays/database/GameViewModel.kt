package com.changrojasalex1168165.simonsays.database

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.changrojasalex1168165.simonsays.dto.GameResult
import com.changrojasalex1168165.simonsays.states.GameResultsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class GameViewModel(
    private val dao: GameResultDao
) : ViewModel(){
    private var state by mutableStateOf( GameResultsState())
        private set

    private val _gameResults = MutableStateFlow<List<GameResult>>(emptyList())
    val gameResults: StateFlow<List<GameResult>> get() = _gameResults



    init {
        viewModelScope.launch {
            dao.getAllGameResults().collectLatest{
                state = state.copy(
                    gameResultList = it
                )
            }

            getResultsFromDatabase()
        }
    }

    fun addGameResult(gameResult: GameResult) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingResults = dao.getGameResultByDetails(
                gameResult.playerName,
                gameResult.round,
                gameResult.timeElapsed,
                gameResult.dateTime
            )

            if (existingResults.isEmpty()) {
                dao.insertGameResult(gameResult)
            }
        }
    }

    private fun getResultsFromDatabase(){

        viewModelScope.launch (Dispatchers.IO){
            dao.getAllGameResults().collect { gameResults ->
                Log.d("Scores that won", gameResults.toString())
                _gameResults.value = gameResults
            }
        }
    }

    fun getGameResults(): MutableStateFlow<List<GameResult>> {
        getResultsFromDatabase()
        return _gameResults
    }





}