package com.example.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {
    @Query("SELECT * from movies order by popularity desc")
    fun fetchMovieList(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(list: List<Movie?>?)

    @Query("DELETE FROM movies")
    fun deleteMovies()

    @Transaction
    fun deleteAndInsertMovieList(listItem: List<Movie?>?) {
        deleteMovies()
        insertMovies(list = listItem)
    }
}