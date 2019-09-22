package com.example.movieapp.database

import com.google.gson.annotations.SerializedName

data class TrailerResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("results")
	val results: List<MovieTrailer>? = null
)