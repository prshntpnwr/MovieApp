package com.example.movieapp.observer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.example.movieapp.BuildConfig
import com.example.movieapp.database.Movie
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.util.AbsentedLiveData
import com.example.movieapp.util.Resource
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    repo: AppRepository
) : ViewModel() {

    private val _refID: MutableLiveData<RepoID> = MutableLiveData()
    val refID: LiveData<RepoID>
        get() = _refID

    val result: LiveData<Resource<List<Movie>>> = Transformations
        .switchMap(_refID) { input ->
            input.ifExists { category ->
                repo.fetchMovieList(category)
            }
        }

    fun init(filter: Int) {
        val update = RepoID(filter)
        if (_refID.value == update) return
        _refID.postValue(update)
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

}