package com.example.movieapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.factory.AppModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppModelFactory): ViewModelProvider.Factory

}