package com.example.movieapp.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.movieapp.database.*
import com.example.movieapp.remote.WebService
import com.example.movieapp.ui.MoviesFragment
import com.example.movieapp.util.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val webservice: WebService,
    private val executor: AppExecutors,
    private val dao: AppDao
) {
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
        MoviesFragment.FILTER_TRENDY to popular,
        MoviesFragment.FILTER_RATED to topRated
    )

    fun fetchMovieList(category: Int): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(executor) {
            override fun saveCallResult(item: MoviesResponse) {
                Log.e(Thread.currentThread().name, "fetch_movie: ${Gson().toJson(item)}")
                item.results.let {
                    dao.deleteAndInsertMovieList(listItem = it, category = category)
                }
            }

            override fun shouldFetch(data: List<Movie>?) = data.isNullOrEmpty()

            override fun loadFromDb() = dao.fetchMovieList(category)

            override fun createCall() = webservice.getMovieList(sortBy = map[category]!!)

        }.asLiveData()
    }

    fun fetchMovieWithDetails(movieId: Int): LiveData<Resource<MovieWithDetail>> {
        return object : NetworkBoundResource<MovieWithDetail, MovieDetail>(executor) {
            override fun saveCallResult(item: MovieDetail) {
                dao.insertMovieDetails(item = item)
            }

            override fun shouldFetch(data: MovieWithDetail?) = data == null

            override fun loadFromDb() = dao.loadMovieWithDetail(id = movieId)

            override fun createCall() = webservice.fetchMovieDetails(id = movieId)

        }.asLiveData()
    }

    private fun fetchMovieTrailer(movieId: Int) {
        object : DataBoundResource<List<MovieTrailer>, TrailerResponse>() {
            override fun saveCallResult(item: TrailerResponse) {
                dao.insertMovieTrailerList(item)
            }

            override fun shouldFetch(data: List<MovieTrailer>?) = data.isNullOrEmpty()

            override fun loadFromDb() = dao.loadMovieTrailer(movieId = movieId)

            override fun createCall() = webservice.fetchMovieTrailers(movieId)
        }
    }

    private fun fetchMovieReviews(movieId: Int) {
        object : DataBoundResource<List<Reviews>, ReviewsResponse>() {
            override fun saveCallResult(item: ReviewsResponse) {
                dao.insertReviewList(item)
            }

            override fun shouldFetch(data: List<Reviews>?) = data.isNullOrEmpty()

            override fun loadFromDb() = dao.loadMovieReviews(movieId = movieId)

            override fun createCall() = webservice.fetchMovieReview(movieId)

        }
    }

    private fun fetchMovieCast(movieId: Int) {
        object : DataBoundResource<List<Cast>, CastResponse>() {
            override fun loadFromDb() = dao.loadCast(movieId = movieId)

            override fun shouldFetch(data: List<Cast>?) = true

            override fun createCall() = webservice.fetchCast(movieId)

            override fun saveCallResult(item: CastResponse) {
                dao.insertCastList(item)
            }
        }
    }

    fun fetchMovieData(movieId: Int, scope: CoroutineScope) {
        scope.launch {
            val d = listOf(
                async { fetchMovieCast(movieId) },
                async { fetchMovieTrailer(movieId) },
                async { fetchMovieReviews(movieId) }
            )

            d.awaitAll()
        }
    }

}