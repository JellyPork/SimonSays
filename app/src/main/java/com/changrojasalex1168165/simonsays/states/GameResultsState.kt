package com.changrojasalex1168165.simonsays.states

import com.changrojasalex1168165.simonsays.dto.GameResult

data class GameResultsState(
    val gameResultList: List<GameResult> = emptyList()
)
