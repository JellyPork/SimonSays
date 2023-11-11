package com.changrojasalex1168165.simonsays.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.changrojasalex1168165.simonsays.dto.GameResult

@Database(entities = [GameResult::class], version = 1, exportSchema = false)
abstract class GameResultDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao

}