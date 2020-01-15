package com.example.movieapp.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.os.BuildCompat
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
//        val nightMode = if (BuildCompat.isAtLeastQ()) {
//            MODE_NIGHT_FOLLOW_SYSTEM
//        } else {
//            MODE_NIGHT_AUTO_BATTERY
//        }
//        setDefaultNightMode(nightMode)
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