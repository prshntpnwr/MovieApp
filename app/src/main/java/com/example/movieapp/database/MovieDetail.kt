package com.example.movieapp.database

import androidx.room.Entity
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movie_detail",
    primaryKeys = ["id"])
data class MovieDetail(

    @field:SerializedName("id")
    var id: Int = 0,

    @field:SerializedName("original_language")
    var originalLanguage: String? = null,

    @field:SerializedName("imdb_id")
    var imdbId: String? = null,

    @field:SerializedName("video")
    var video: Boolean? = null,

    @field:SerializedName("title")
    var title: String? = null,

    @field:SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @field:SerializedName("revenue")
    var revenue: Long? = null,

    @field:SerializedName("popularity")
    var popularity: Double? = null,

    @field:SerializedName("vote_count")
    var voteCount: Double? = null,

    @field:SerializedName("budget")
    var budget: Long? = null,

    @field:SerializedName("overview")
    var overview: String? = null,

    @field:SerializedName("original_title")
    var originalTitle: String? = null,

    @field:SerializedName("runtime")
    var runtime: String? = null,

    @field:SerializedName("poster_path")
    var posterPath: String? = null,

    @field:SerializedName("release_date")
    var releaseDate: String? = null,

    @field:SerializedName("vote_average")
    var voteAverage: Double? = null,

    @field:SerializedName("tagline")
    var tagline: String? = null,

    @field:SerializedName("adult")
    var adult: Boolean? = null,

    @field:SerializedName("homepage")
    var homepage: String? = null,

    @field:SerializedName("status")
    var status: String? = null,

    @field:SerializedName("genres")
    var genres: MutableList<MovieGenre?>? = null
)