package com.example.movieapp.di

import android.app.Application
import com.example.movieapp.ui.AppController
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import com.example.movieapp.di.module.ActivityModule
import com.example.movieapp.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: AppController)
}