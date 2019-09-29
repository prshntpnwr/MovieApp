package com.example.movieapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_reviews")
data class Reviews(

	@PrimaryKey
	@field:SerializedName("id")
	var id: String = "",

	var movieId: Int = 0,

	@field:SerializedName("author")
	var author: String? = null,

	@field:SerializedName("content")
	var content: String? = null,

	@field:SerializedName("url")
	var url: String? = null
)