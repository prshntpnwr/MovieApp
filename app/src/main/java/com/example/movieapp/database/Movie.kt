package com.example.movieapp.database

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies",
	primaryKeys = ["id"])
data class Movie(

	@field:SerializedName("id")
	var id: Int = 0,

	@field:SerializedName("overview")
	var overview: String? = null,

	@field:SerializedName("original_language")
	var originalLanguage: String? = null,

	@field:SerializedName("original_title")
	var originalTitle: String? = null,

	@field:SerializedName("video")
	var video: Boolean? = null,

	@field:SerializedName("title")
	var title: String? = null,

	@field:SerializedName("poster_path")
	var posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	var backdropPath: String? = null,

	@field:SerializedName("release_date")
	var releaseDate: String? = null,

	@field:SerializedName("vote_average")
	var voteAverage: Double? = null,

	@field:SerializedName("popularity")
	var popularity: Double? = null,

	@field:SerializedName("adult")
	var adult: Boolean? = null,

	@field:SerializedName("vote_count")
	var voteCount: Double? = null
)