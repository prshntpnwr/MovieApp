package com.example.movieapp.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.movieapp.database.AppDatabase
import com.example.movieapp.database.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    // database injection
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGithubDao(database: AppDatabase) = database.appDao()

    @Provides
    @Singleton
    fun providePrefManager(application: Application) = PreferenceManager.getInstance(application.baseContext)
}