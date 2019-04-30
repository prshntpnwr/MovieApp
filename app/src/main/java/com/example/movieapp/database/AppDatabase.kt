package com.example.movieapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [(Movie::class)],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

}