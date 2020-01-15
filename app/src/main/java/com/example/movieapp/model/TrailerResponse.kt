package com.example.movieapp.model

import com.example.movieapp.database.MovieTrailer
import com.google.gson.annotations.SerializedName

data class TrailerResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("results")
	val results: List<MovieTrailer>? = null
)