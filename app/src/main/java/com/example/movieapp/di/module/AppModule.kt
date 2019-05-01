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

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    // database injection
    @Provides
    @Singleton
    fun provideDatabaseModule(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun webServiceHolderModule(): WebServiceHolder {
        return WebServiceHolder.instance
    }

    @Provides
    fun provideExecutorModule(): AppExecutors = AppExecutors()

    @Provides
    fun provideGsonModule(): Gson = GsonBuilder().create()

    @Provides
    fun provideAppRepository(webService: WebService, executors: AppExecutors, appDao: AppDao): AppRepository {
        return AppRepository(webService, executors, appDao)
    }

    @Provides
    @Singleton
    fun provideGithubDao(database: AppDatabase) = database.appDao()

    @SuppressLint("NewApi")
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        okHttpClient.sslSocketFactory()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiWebservice(restAdapter: Retrofit): WebService {
        val webService = restAdapter.create(WebService::class.java)
        WebServiceHolder.instance.setAPIService(webService)
        return webService
    }
}