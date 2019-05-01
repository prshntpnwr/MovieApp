package com.example.movieapp.observer

import androidx.lifecycle.ViewModel;
import com.example.movieapp.repo.AppRepository
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    repo: AppRepository
) : ViewModel() {

}
