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
    val repo: AppRepository
) : ViewModel(), Observable {

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

    val result: LiveData<Resource<MovieWithDetail>> = Transformations
        .switchMap(_refID) { input ->
            input.ifExists { id, _ -> repo.fetchMovieWithDetails(movieId = id) }
        }

    private val _refID1: MutableLiveData<RepoID> = MutableLiveData()
    val trailer: LiveData<Resource<List<MovieTrailer>>> = Transformations
        .switchMap(_refID1) { input ->
            input.ifExists { id, _ -> repo.fetchMovieTrailer(movieId = id) }
        }

    private val _refID2: MutableLiveData<RepoID> = MutableLiveData()
    val review: LiveData<Resource<List<Reviews>>> = Transformations
        .switchMap(_refID2) { input ->
            input.ifExists { id, _ -> repo.fetchMovieReviews(movieId = id) }
        }

    fun init(refID: Int) {
        _refID.postValue(RepoID(refID, CALL_DETAIL))
        _refID1.postValue(RepoID(refID, CALL_TRAILER))
        _refID2.postValue(RepoID(refID, 3))
    }

    fun notifyViews() {
        notifyPropertyChanged(BR.detailStatus)
        notifyPropertyChanged(BR.movieDetail)
        notifyPropertyChanged(BR.trailerVisible)
        notifyPropertyChanged(BR.reviewsVisible)
    }

    data class RepoID(val refID: Int?, val call: Int) {
        fun <T> ifExists(f: (Int, Int) -> LiveData<T>): LiveData<T> {
            return if (refID == null) {
                AbsentedLiveData.create()
            } else {
                f(refID, call)
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
//    private val handler = CoroutineExceptionHandler { _, exception ->
//        Log.d(Thread.currentThread().name, "$exception handled !")
//    }
//
//    lateinit var job: Job
//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.Main + job + handler
//
//    fun fetchTask() {
//        GlobalScope.launch(coroutineContext) {
//            val movie = async(Dispatchers.IO + handler) { fetchMovie() }
//            val res = async(Dispatchers.IO + handler) { saveMovie(movie.await()) }
//        }
//    }
//
//    fun customScope() {
//        launch {
//            val movie = getMovie()
//        }
//
//
//        // or
//        val id = async {
//            val movie = getMovie()
//            movie.id
//
//        }
//
//        // or
//        launch {
//            val deffered = listOf(
//                async { getMovie() },
//                async { getMovie() }
//            )
//
//            deffered.awaitAll()
//        }
//    }
//
//    suspend fun fetchTask2(): Movie {
//        return withContext(coroutineContext) {
//            getMovie()
//        }
//    }
//
//    fun dispatchAction(id: Int) = liveData {
//        emit(repo.fetchMovieWithDetails(id))
//    }
//
//    private suspend fun fetchMovie(): Movie {
//        return Movie()
//    }
//
//    private suspend fun saveMovie(movie: Movie) {
//
//    }
//
//    private fun getMovie(): Movie {
//        return Movie()
//    }
}
