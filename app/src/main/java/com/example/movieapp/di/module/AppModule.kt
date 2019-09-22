package com.example.movieapp.di.module

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Room
import com.example.movieapp.database.AppDao
import com.example.movieapp.database.AppDatabase
import com.example.movieapp.remote.WebServiceHolder
import com.example.movieapp.remote.WebService
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.LiveDataCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [
    (ViewModelModule::class),
    (NetworkModule::class)
])
class AppModule {

    // database injection
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun webServiceHolder(): WebServiceHolder {
        return WebServiceHolder.instance
    }

    @Provides
    fun provideExecutor(): AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideGithubDao(database: AppDatabase) = database.appDao()

    @Provides
    @Singleton
    fun provideAppRepository(webservice: WebService, executor: AppExecutors, dao: AppDao): AppRepository {
        return AppRepository(webservice, executor, dao)
    }
}