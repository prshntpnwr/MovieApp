package com.example.movieapp.observer

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import com.example.movieapp.database.Movie
import com.example.movieapp.model.MovieHolder
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.util.AbsentedLiveData
import com.example.movieapp.util.Resource
import com.example.movieapp.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MoviesViewModel @Inject constructor(
    val repo: AppRepository
) : ViewModel(), Observable, CoroutineScope {

    var currentTheme = MODE_NIGHT_NO
    private val _refID = MutableLiveData<Boolean>()
    val listStatus: Status?
        @Bindable get() = result.value?.status

    val result: LiveData<Resource<List<Movie>>> =
        Transformations.switchMap(_refID) { repo.loadMovies() }

    init {
        _refID.value = true
    }

    fun loadNow() {
        repo.loadAllMovies(this)
    }

    fun updateStatus() {
        notifyPropertyChanged(BR.listStatus)
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

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(Thread.currentThread().name, "$exception handled !")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob() + handler
}