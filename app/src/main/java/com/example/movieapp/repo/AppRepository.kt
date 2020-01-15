package com.example.movieapp.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.movieapp.database.*
import com.example.movieapp.model.*
import com.example.movieapp.remote.WebService
import com.example.movieapp.ui.MoviesFragment
import com.example.movieapp.util.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val webservice: WebService,
    private val executor: AppExecutors,
    private val dao: AppDao
) {
    private val popular: HashMap<String, String> = hashMapOf("sort_by" to "popularity.desc")
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

    fun loadAllMovies(scope: CoroutineScope) {
        Log.e(this.javaClass.name, "loadAllMovies ${map.size}")
        scope.launch {
            for ((k, _) in map) {
                withContext(Dispatchers.Default) {
                    fetchMovie(k)
                }
            }
        }
    }

    fun loadMovies(): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(executor) {
            override fun saveCallResult(item: MoviesResponse) {
                TODO()
            }

            override fun shouldFetch(data: List<Movie>?) = false

            override fun loadFromDb() = dao.loadMovies()

            override fun createCall(): LiveData<ApiResponse<MoviesResponse>> {
                TODO()
            }

        }.asLiveData()
    }

    private fun fetchMovie(category: Int) {
        object : DataBoundResource<List<Movie>, MoviesResponse>() {
            override fun saveCallResult(item: MoviesResponse) {
                Log.e(this.javaClass.name, "fetchMovie: ${Gson().toJson(item)}")
                item.results.let {
                    dao.insertMovieList(listItem = it, category = category)
                }
            }

            override fun shouldFetch(data: List<Movie>?) = true

            override fun loadFromDb() = dao.fetchMoviesByCategory(category)

            override fun createCall() = webservice.getMovies(sortBy = map[category]!!)
        }
    }

    fun fetchMovieList(category: Int): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, MoviesResponse>(executor) {
            override fun saveCallResult(item: MoviesResponse) {
                item.results.let {
                    dao.insertMovieList(listItem = it, category = category)
                }
            }

            override fun shouldFetch(data: List<Movie>?) = data.isNullOrEmpty()

            override fun loadFromDb() = dao.fetchMovieList(category)

            override fun createCall() = webservice.getMovieList(sortBy = map[category]!!)

        }.asLiveData()
    }

    fun loadMovieFromSource(): LiveData<List<MovieHolder>> {
        Log.e(javaClass.name, "loadMovieFromSource")
        return object : AdapterFactory<List<Movie>, List<MovieHolder>>() {
            override fun adapt(data: List<Movie>): List<MovieHolder> {
                Log.e(javaClass.name, "loadMovieFromSource_adapt: ${Gson().toJson(data)}")
                val holder = emptyList<MovieHolder>() as MutableList
                repeat(data.size) {
                    holder.add(MovieHolder("Title", data.filter { it.category == 0 } ))
                }

                return holder
            }

            override fun dataSource(): LiveData<List<Movie>> {
                return dao.loadMovies()
            }

        }.asLiveData()
    }

    fun loadMovie() = liveData {
        val m = dao.loadMovies()
        val m1 = withContext(Dispatchers.IO) {dao.fetchMoviesByCategory(0)}
        val m2 = m.value

        emit(movies(m2, m1))
    }

    fun movies(m: List<Movie>?, m1: List<Movie>?): List<MovieHolder> {
        val holder = emptyList<MovieHolder>().toMutableList()
        m?.mapIndexed { _, _ -> holder.add(MovieHolder("Title", m)) }
        Log.e(javaClass.name, "on_conversion: ${m?.size} and ${holder.size} ${m1?.size}")
        return holder
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

    fun fetchMovieTrailer(movieId: Int) {
        object : DataBoundResource<List<MovieTrailer>, TrailerResponse>() {
            override fun saveCallResult(item: TrailerResponse) {
                dao.insertMovieTrailerList(item)
            }

            override fun shouldFetch(data: List<MovieTrailer>?) = data.isNullOrEmpty()

            override fun loadFromDb() = dao.loadMovieTrailer(movieId = movieId)

            override fun createCall() = webservice.fetchMovieTrailers(movieId)
        }
    }

    fun fetchMovieReviews(movieId: Int) {
        object : DataBoundResource<List<Reviews>, ReviewsResponse>() {
            override fun saveCallResult(item: ReviewsResponse) {
                dao.insertReviewList(item)
            }

            override fun shouldFetch(data: List<Reviews>?) = data.isNullOrEmpty()

            override fun loadFromDb() = dao.loadMovieReviews(movieId = movieId)

            override fun createCall() = webservice.fetchMovieReview(movieId)
        }
    }

    fun fetchMovieCast(movieId: Int) {
        object : DataBoundResource<List<Cast>, CastResponse>() {
            override fun loadFromDb() = dao.loadCast(movieId = movieId)

            override fun shouldFetch(data: List<Cast>?) = true

            override fun createCall() = webservice.fetchCast(movieId)

            override fun saveCallResult(item: CastResponse) {
                dao.insertCastList(item)
            }
        }
    }
}