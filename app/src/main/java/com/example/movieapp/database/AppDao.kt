package com.example.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {
    @Query("SELECT * from movies WHERE category = :category order by popularity desc")
    fun fetchMovieList(category: Int): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(list: List<Movie?>?)

    @Query("DELETE FROM movies")
    fun deleteMovies()

    @Transaction
    fun deleteAndInsertMovieList(listItem: List<Movie?>?, category: Int) {
        listItem?.map { m -> m?.category = category}
        insertMovies(list = listItem)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetails(item: MovieDetail)

    @Query("SELECT * FROM movie_detail where id = :id")
    fun fetchMovieDetails(id: Int) :LiveData<MovieDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieTrailer(items: List<MovieTrailer>?)

    @Transaction
    fun insertMovieTrailerList(item: TrailerResponse) {
        item.results?.mapIndexed { _, trailer ->
            trailer.movieId = item.id ?: 0
        }

        insertMovieTrailer(item.results)
    }

    @Query("SELECT * FROM movie_trailer where movieId = :id")
    fun loadMovieTrailers(id: Int): LiveData<List<MovieTrailer>>
}