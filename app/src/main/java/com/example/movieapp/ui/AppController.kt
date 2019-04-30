package com.example.movieapp.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import com.example.movieapp.di.AppInjector
import javax.inject.Inject

class AppController : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppInjector.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /**
     * Returns an [AndroidInjector] of [Activity]s.
     */
    override fun activityInjector() = dispatchingAndroidInjector

    companion object {
        @get:Synchronized
        lateinit var instance: AppController
    }

}