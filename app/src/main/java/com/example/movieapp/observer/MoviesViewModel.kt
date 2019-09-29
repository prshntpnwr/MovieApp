package com.example.movieapp.observer

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.example.movieapp.BuildConfig
import com.example.movieapp.database.Movie
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.util.AbsentedLiveData
import com.example.movieapp.util.Resource
import com.example.movieapp.util.Status
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    repo: AppRepository
) : ViewModel(), Observable {

    private var shouldFetch = true
    private val _refID: MutableLiveData<RepoID> = MutableLiveData()
    val refID: LiveData<RepoID>
        get() = _refID
    val listStatus: Status?
        @Bindable get() = result.value?.status

    val result: LiveData<Resource<List<Movie>>> = Transformations
        .switchMap(_refID) { input ->
            input.ifExists { category ->
                repo.fetchMovieList(category)
            }
        }

    fun fetchTask(filter: Int) {
        if (!shouldFetch) return
        val update = RepoID(filter)
        if (_refID.value == update) return
        _refID.postValue(update)
    }

    fun updateFetch(flag: Boolean) {
        shouldFetch = flag
        notifyPropertyChanged(BR.listStatus)
    }

    data class RepoID(val filter: Int?) {
        fun <T> ifExists(f: (Int) -> LiveData<T>): LiveData<T> {
            return if (filter == null) {
                AbsentedLiveData.create()
            } else {
                f(filter)
            }
        }
    }

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
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
}