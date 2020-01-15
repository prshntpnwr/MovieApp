package com.example.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movieapp.model.CastResponse
import com.example.movieapp.model.ReviewsResponse
import com.example.movieapp.model.TrailerResponse

@Dao
interface AppDao {
    @Query("SELECT * from movies")
    fun loadMovieList(): LiveData<List<Movie>>

    @Query("SELECT * from movies")
    fun loadMovies(): LiveData<List<Movie>>

    @Query("SELECT * from movies WHERE category = :category order by popularity desc")
    fun fetchMovieList(category: Int): LiveData<List<Movie>>

    @Query("SELECT * from movies WHERE category = :category order by popularity desc")
    fun fetchMoviesByCategory(category: Int): List<Movie>

    @Query("SELECT * FROM movie_detail where id = :id")
    fun loadMovieWithDetail(id: Int): LiveData<MovieWithDetail>

    @Query("SELECT * FROM movie_trailer where movieId = :movieId")
    fun loadMovieTrailer(movieId: Int): List<MovieTrailer>

    @Query("SELECT * FROM movie_reviews where movieId = :movieId")
    fun loadMovieReviews(movieId: Int): List<Reviews>

    @Query("SELECT * FROM movie_cast where movieId = :movieId")
    fun loadMovieCast(movieId: Int): LiveData<List<Cast>>

    @Query("SELECT * FROM movie_cast where movieId = :movieId")
    fun loadCast(movieId: Int): List<Cast>

    @Query("SELECT * FROM movie_cast where movieId = :movieId")
    fun loadSimilar(movieId: Int): List<Cast>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(list: List<Movie?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieTrailer(items: List<MovieTrailer>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(items: List<Reviews?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCast(items: List<Cast?>?)

    @Query("DELETE FROM movies")
    fun deleteMovies()

    @Transaction
    fun insertMovieList(listItem: List<Movie?>?, category: Int) {
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

    @Transaction
    fun insertCastList(item: CastResponse) {
        item.results?.mapIndexed { _, cast -> cast.movieId = item.id ?: 0 }
        insertCast(item.results)
    }
}