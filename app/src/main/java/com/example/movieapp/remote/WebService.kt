package com.example.movieapp.remote

import androidx.lifecycle.LiveData
import com.example.movieapp.database.*
import com.example.movieapp.model.CastResponse
import com.example.movieapp.model.MoviesResponse
import com.example.movieapp.model.ReviewsResponse
import com.example.movieapp.model.TrailerResponse
import com.example.movieapp.util.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.util.HashMap

interface WebService {

    @GET("discover/movie")
    fun getMovieList(
        @QueryMap sortBy: HashMap<String, String>
    ): LiveData<ApiResponse<MoviesResponse>>

    @GET("discover/movie")
    fun getMovies(
        @QueryMap sortBy: HashMap<String, String>
    ): Call<MoviesResponse>

    @GET("movie/{id}")
    fun fetchMovieDetails(
        @Path("id") id: Int
    ): LiveData<ApiResponse<MovieDetail>>

    @GET("movie/{id}/videos")
    fun fetchMovieTrailers(
        @Path("id") id: Int
    ): Call<TrailerResponse>

    @GET("movie/{id}/reviews")
    fun fetchMovieReview(
        @Path("id") id: Int
    ): Call<ReviewsResponse>

    // coroutine calls
    @GET("movie/{id}/casts")
    fun fetchCast(
        @Path("id") id: Int
    ): Call<CastResponse>

    @GET("movie/{movie_id}/similar")
    fun fetchSimilarMovies(
        @Path("id") id: Int
    ): Call<MoviesResponse>
}