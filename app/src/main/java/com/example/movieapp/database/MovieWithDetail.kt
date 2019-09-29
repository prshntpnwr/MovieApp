package com.example.movieapp.database

import androidx.room.Embedded
import androidx.room.Relation

class MovieWithDetail {
    @Embedded
    var movie: MovieDetail? = null

    @Relation(parentColumn = "id", entityColumn = "movieId")
    var trailers: List<MovieTrailer>? = null

    @Relation(parentColumn = "id", entityColumn = "movieId")
    var reviews: List<Reviews>? = null
}