package com.example.movieapp.repo

import androidx.lifecycle.LiveData
import com.example.movieapp.BuildConfig
import com.example.movieapp.database.AppDao
import com.example.movieapp.database.Movie
import com.example.movieapp.database.MovieDetail
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
) {
    private val apiKey = BuildConfig.ApiKey
    val map: HashMap<Int, String?> = hashMapOf(
        0 to "popularity.desc",
        1 to "vote_average.desc"
    )

    fun fetchMovieList(category: Int): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(executor) {
            override fun saveCallResult(item: MoviesResponse) {
                item.results.let {
                    dao.deleteAndInsertMovieList(listItem = it)
                }
            }

            override fun shouldFetch(data: List<Movie>?) = true

            override fun loadFromDb() = dao.fetchMovieList()

            override fun createCall() = webservice.getMovieList(apiKey = apiKey, sortBy = map[category] ?: "")

        }.asLiveData()
    }

    fun fetchMovieDetails(id: Int): LiveData<Resource<MovieDetail>> {
        return object : NetworkBoundResource<MovieDetail, MovieDetail>(executor) {
            override fun saveCallResult(item: MovieDetail) {
                dao.insertMovieDetails(item = item)
            }

            override fun shouldFetch(data: MovieDetail?) = true

            override fun loadFromDb() = dao.fetchMovieDetails(id = id)

            override fun createCall() = webservice.fetchMovieDetails(id = id, apiKey = apiKey)

        }.asLiveData()
    }

}