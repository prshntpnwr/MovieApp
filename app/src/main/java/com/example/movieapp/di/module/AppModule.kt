package com.example.movieapp.di.module

import com.example.movieapp.database.AppDao
import com.example.movieapp.remote.WebService
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.ui.AppController
import com.example.movieapp.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    (ViewModelModule::class),
    (NetworkModule::class),
    (DatabaseModule::class)
])
class AppModule {

    @Provides
    fun provideApplication(): AppController = AppController.instance

    @Provides
    fun provideExecutor(): AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideAppRepository(webservice: WebService, executor: AppExecutors, dao: AppDao): AppRepository {
        return AppRepository(webservice, executor, dao)
    }
}