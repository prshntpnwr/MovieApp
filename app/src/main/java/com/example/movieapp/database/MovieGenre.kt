package com.example.movieapp.database

import com.google.gson.annotations.SerializedName

data class MovieGenre(

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("name")
    var name: String? = null

)
