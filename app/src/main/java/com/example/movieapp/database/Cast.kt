package com.example.movieapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_cast")
data class Cast (
    @PrimaryKey
    @field:SerializedName("cast_id")
    var id: String = "",

    var movieId: Int = 0,

    @field:SerializedName("character")
    var character: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("profile_path")
    var profilePath: String? = null
) {
    fun getProfilePicture() = if (profilePath == null) null else "https://image.tmdb.org/t/p/w500$profilePath"
}