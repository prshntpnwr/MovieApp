package com.example.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {
    @Query("SELECT * from movies WHERE category = :category order by popularity desc")
    fun fetchMovieList(category: Int): LiveData<List<Movie>>

    @Query("SELECT * FROM movie_detail where id = :id")
    fun loadMovieWithDetail(id: Int): LiveData<MovieWithDetail>

    @Query("SELECT * FROM movie_trailer where movieId = :movieId")
    fun loadMovieTrailer(movieId: Int): LiveData<List<MovieTrailer>>

    @Query("SELECT * FROM movie_reviews where movieId = :movieId")
    fun loadMovieReviews(movieId: Int): LiveData<List<Reviews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(list: List<Movie?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieTrailer(items: List<MovieTrailer>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(items: List<Reviews?>?)

    @Query("DELETE FROM movies")
    fun deleteMovies()

    @Transaction
    fun deleteAndInsertMovieList(listItem: List<Movie?>?, category: Int) {
        listItem?.map { m -> m?.category = category }
        insertMovies(list = listItem)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetails(item: MovieDetail)

    @Query("SELECT * FROM movie_detail where id = :id")
    fun fetchMovieDetails(id: Int) :LiveData<MovieDetail>

    @Query("SELECT * FROM movie_trailer where movieId = :id")
    fun loadMovieTrailers(id: Int): LiveData<List<MovieTrailer>>

    @Transaction
    fun insertMovieTrailerList(item: TrailerResponse) {
        item.results?.mapIndexed { _, trailer -> trailer.movieId = item.id ?: 0 }
        insertMovieTrailer(item.results)
    }

    @Transaction
    fun insertReviewList(item: ReviewsResponse) {
        item.results?.mapIndexed { _, review -> review?.movieId = item.id ?: 0 }
        insertReviews(item.results)
    }
}