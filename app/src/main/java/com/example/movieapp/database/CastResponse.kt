package com.example.movieapp.database

import com.google.gson.annotations.SerializedName

data class CastResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("cast")
	val results: List<Cast>? = null
)