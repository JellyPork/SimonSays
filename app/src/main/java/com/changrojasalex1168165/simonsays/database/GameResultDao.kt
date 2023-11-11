package com.changrojasalex1168165.simonsays.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.changrojasalex1168165.simonsays.dto.GameResult
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Insert
    suspend fun insertGameResult(gameResult: GameResult)

    @Query("SELECT * FROM GameResult")
    fun getAllGameResults(): Flow<List<GameResult>>

    @Query("SELECT * FROM GameResult WHERE player_text = :playerName AND player_round = :round AND time_elapsed = :timeElapsed AND match_date = :dateTime")
    fun getGameResultByDetails(playerName: String, round: Int, timeElapsed: String, dateTime: String): List<GameResult>
}