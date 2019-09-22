package com.example.movieapp.database

import androidx.room.Embedded
import androidx.room.Relation

class MovieWithDetail {
    @Embedded
    var movie: Movie? = null

    @Relation(parentColumn = "id", entityColumn = "id")
    var detail: List<MovieDetail>? = null

    @Relation(parentColumn = "id", entityColumn = "movieId")
    var trailers: List<MovieTrailer>? = null

}