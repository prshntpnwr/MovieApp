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
    private val popular: HashMap<String, String> = hashMapOf(
        "sort_by" to "popularity.desc"
    )

    private val topRated: HashMap<String, String> = hashMapOf(
        "sort_by" to "vote_average.desc",
        "vote_count.gte" to "500",
        "certification_country" to "US",
        "certification" to "R"
    )

    val map: HashMap<Int, HashMap<String, String>> = hashMapOf(
        0 to popular,
        1 to topRated
    )

    fun fetchMovieList(category: Int): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(executor) {
            override fun saveCallResult(item: MoviesResponse) {
                item.results.let {
                    dao.deleteAndInsertMovieList(listItem = it)
                }
            }

            override fun shouldFetch(data: List<Movie>?) = data == null

            override fun loadFromDb() = dao.fetchMovieList()

            override fun createCall() = webservice.getMovieList(apiKey = apiKey, sortBy = map[category]!!)

        }.asLiveData()
    }

    fun fetchMovieDetails(id: Int): LiveData<Resource<MovieDetail>> {
        return object : NetworkBoundResource<MovieDetail, MovieDetail>(executor) {
            override fun saveCallResult(item: MovieDetail) {
                dao.insertMovieDetails(item = item)
            }

            override fun shouldFetch(data: MovieDetail?) = data == null

            override fun loadFromDb() = dao.fetchMovieDetails(id = id)

            override fun createCall() = webservice.fetchMovieDetails(id = id, apiKey = apiKey)

        }.asLiveData()
    }

}