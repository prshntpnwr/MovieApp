package com.example.movieapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson

class ConverterFactory {

    @TypeConverter
    fun listToJson(value: MutableList<MovieGenre?>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String?): MutableList<MovieGenre?>? {

        val objects = Gson().fromJson(value, Array<MovieGenre?>::class.java)
        val list = objects?.toMutableList()
        return list
    }
}
