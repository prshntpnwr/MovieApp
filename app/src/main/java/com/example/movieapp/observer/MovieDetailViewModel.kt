package com.example.movieapp.observer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.example.movieapp.database.MovieDetail
import com.example.movieapp.repo.AppRepository
import com.example.movieapp.util.AbsentedLiveData
import com.example.movieapp.util.Resource
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    repo: AppRepository
) : ViewModel() {

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
}
