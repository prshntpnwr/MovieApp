package com.example.movieapp.database

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "movie_trailer",
    primaryKeys = ["id","movieId"]
)
data class MovieTrailer(

    var movieId: Int = 0,

    @field:SerializedName("id")
    var id: String = "0",

    @field:SerializedName("site")
    var site: String? = null,

    @field:SerializedName("size")
    var size: Int? = null,

    @field:SerializedName("iso_3166_1")
    var iso31661: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("type")
    var type: String? = null,

    @field:SerializedName("iso_639_1")
    var iso6391: String? = null,

    @field:SerializedName("key")
    var key: String? = null
) {

    fun getTrailerPreview() = "http://img.youtube.com/vi/$key/mqdefault.jpg"

    fun getTitle() = "$name \u2022 $type"

    fun getSubTitle() = "$site \u2022 ${size}p"

}