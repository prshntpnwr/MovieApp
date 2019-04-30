package com.example.movieapp.di.module

import android.app.Application
import androidx.room.Room
import com.example.movieapp.database.AppDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    @Singleton
    fun provideDatabaseModule(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "movie_database")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    fun provideGsonModule(): Gson = GsonBuilder().create()

}