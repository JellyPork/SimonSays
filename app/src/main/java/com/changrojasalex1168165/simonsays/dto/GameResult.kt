package com.changrojasalex1168165.simonsays.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GameResult")
data class GameResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "player_text")
    val playerName: String,
    @ColumnInfo(name = "player_round")
    val round: Int,
    @ColumnInfo(name = "time_elapsed")
    val timeElapsed: String,
    @ColumnInfo(name = "match_date")
    val dateTime: String
)