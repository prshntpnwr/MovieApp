package com.example.movieapp.observer

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import com.example.movieapp.database.*
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.ui.MovieDetailFragment.Companion.CALL_DETAIL
import com.example.movieapp.ui.MovieDetailFragment.Companion.CALL_TRAILER
import com.example.movieapp.util.AbsentedLiveData
import com.example.movieapp.util.ConverterUtil
import com.example.movieapp.util.Resource
import com.example.movieapp.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel @Inject constructor(
    private val repo: AppRepository
) : ViewModel(), Observable, CoroutineScope {

    private val _refID: MutableLiveData<RepoID> = MutableLiveData()
    val refID: LiveData<RepoID>
        get() = _refID
    val movieDetail: MovieDetail?
        @Bindable get() = result.value?.data?.movie
    val detailStatus: Status?
        @Bindable get() = result.value?.status
    val trailerVisible: Boolean?
        @Bindable get() = result.value?.data?.trailers?.isNullOrEmpty() == false
    val reviewsVisible: Boolean?
        @Bindable get() = result.value?.data?.reviews?.isNullOrEmpty() == false
    val castVisible: Boolean?
        @Bindable get() = result.value?.data?.cast?.isNullOrEmpty() == false

    val result: LiveData<Resource<MovieWithDetail>> = Transformations
        .switchMap(_refID) { input ->
            input.ifExists { id -> repo.fetchMovieWithDetails(movieId = id) }
        }

    fun init(refID: Int) {
        _refID.postValue(RepoID(refID))
        repo.fetchMovieData(refID, this)
    }

    fun notifyViews() {
        notifyPropertyChanged(BR.detailStatus)
        notifyPropertyChanged(BR.movieDetail)
        notifyPropertyChanged(BR.trailerVisible)
        notifyPropertyChanged(BR.reviewsVisible)
        notifyPropertyChanged(BR.castVisible)
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

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.remove(callback)
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Coroutines
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(Thread.currentThread().name, "$exception handled !")
    }

    var job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job +  handler

/*    fun fetchTask() {
        GlobalScope.launch(coroutineContext) {
            val movie = async(Dispatchers.IO + handler) { fetchMovie() }
            val res = async(Dispatchers.IO + handler) { saveMovie(movie.await()) }
        }
    }

    fun customScope() {
        repo.fetchMovieData(this)
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
            val d = listOf(
                async { repo.fetchReviews(0) },
                async { repo.fetchTrailers(0) },
                async { repo.fetchCast(0) }
            )

            d.awaitAll()
        }
    }

    suspend fun fetchTask2(): Movie {
        return withContext(coroutineContext) {
            getMovie()
        }
    }

    fun dispatchAction(id: Int) = liveData {
        emit(repo.fetchMovieWithDetails(id))
    }

    private suspend fun fetchMovie(): Movie {
        return Movie()
    }

    private suspend fun saveMovie(movie: Movie) {

    }

    private fun getMovie(): Movie {
        return Movie()
    }*/
}
