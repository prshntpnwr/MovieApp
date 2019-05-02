package com.example.movieapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [(Movie::class), (MovieDetail::class)],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConverterFactory::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}