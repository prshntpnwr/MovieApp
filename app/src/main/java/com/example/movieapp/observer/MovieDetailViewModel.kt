package com.example.movieapp.observer

import android.util.Log
import androidx.lifecycle.*
import com.example.movieapp.database.Movie
import com.example.movieapp.database.MovieDetail
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.util.AbsentedLiveData
import com.example.movieapp.util.Resource
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel @Inject constructor(
    val repo: AppRepository
) : ViewModel(), CoroutineScope {

    private val _refID: MutableLiveData<RepoID> = MutableLiveData()
    val refID: LiveData<RepoID>
        get() = _refID

    val result: LiveData<Resource<MovieDetail>> = Transformations
        .switchMap(_refID) { input ->
            input.ifExists { id ->
                repo.fetchMovieDetails(id = id)
            }
        }

    fun init(refID: Int) {
        val update = RepoID(refID)
        if (_refID.value == update) return
        _refID.value = update
    }

    data class RepoID(val refID: Int?) {
        fun <T> ifExists(f: (Int) -> LiveData<T>): LiveData<T> {
            return if (refID == null) {
                AbsentedLiveData.create()
            } else {
                f(refID)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Coroutines
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(Thread.currentThread().name, "$exception handled !")
    }

    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + handler

    fun fetchTask() {
        GlobalScope.launch(coroutineContext) {
            val movie = async(Dispatchers.IO + handler) { fetchMovie() }
            val res = async(Dispatchers.IO + handler) { saveMovie(movie.await()) }
        }
    }

    fun customScope() {
        launch {
            val movie = getMovie()
        }


        // or
        val id = async {
            val movie = getMovie()
            movie.id

        }

        // or
        launch {
            val deffered = listOf(
                async { getMovie() },
                async { getMovie() }
            )

            deffered.awaitAll()
        }
    }

    suspend fun fetchTask2(): Movie {
        return withContext(coroutineContext) {
            getMovie()
        }
    }

    fun dispatchAction(id: Int) = liveData {
        emit(repo.fetchMovieDetails(id))
    }

    private suspend fun fetchMovie(): Movie {
        return Movie()
    }

    private suspend fun saveMovie(movie: Movie) {

    }

    private fun getMovie(): Movie {
        return Movie()
    }
}
