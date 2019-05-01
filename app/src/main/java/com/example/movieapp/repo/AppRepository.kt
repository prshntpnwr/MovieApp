package com.example.movieapp.repo

import androidx.lifecycle.LiveData
import com.example.movieapp.BuildConfig
import com.example.movieapp.database.AppDao
import com.example.movieapp.database.Movie
import com.example.movieapp.database.MoviesResponse
import com.example.movieapp.remote.WebService
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.NetworkBoundResource
import com.example.movieapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val webservice: WebService,
    private val executor: AppExecutors,
    private val dao: AppDao
){
    private val apiKey = BuildConfig.ApiKey

    fun fetchMovieList(): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(executor) {
            override fun saveCallResult(item: MoviesResponse) {
                item.results.let {
                    dao.deleteAndInsertMovieList(listItem = it)
                }
            }

            override fun shouldFetch(data: List<Movie>?) = true

            override fun loadFromDb() = dao.fetchMovieList()

            override fun createCall() = webservice.getMovieList(api_key = apiKey)

        }.asLiveData()
    }

}