package com.example.movieapp.model

import com.example.movieapp.database.Movie

data class MovieHolder (

    var title: String? = null,

    var movies: List<Movie?>? = null

)