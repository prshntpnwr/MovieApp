package com.example.movieapp.repo

import com.example.movieapp.database.AppDao
import com.example.movieapp.remote.WebService
import com.example.movieapp.util.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val webservice: WebService,
    private val executor: AppExecutors,
    private val dao: AppDao
){


}