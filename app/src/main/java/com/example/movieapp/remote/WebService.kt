package com.example.movieapp.remote

import androidx.lifecycle.LiveData
import com.example.movieapp.database.MovieDetail
import com.example.movieapp.database.MoviesResponse
import com.example.movieapp.database.ReviewsResponse
import com.example.movieapp.database.TrailerResponse
import com.example.movieapp.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.HashMap

interface WebService {

    @GET("discover/movie")
    fun getMovieList(
        @QueryMap sortBy: HashMap<String, String>
    ): LiveData<ApiResponse<MoviesResponse>>

    @GET("movie/{id}")
    fun fetchMovieDetails(
        @Path("id") id: Int
    ): LiveData<ApiResponse<MovieDetail>>

    @GET("movie/{id}/videos")
    fun fetchMovieTrailers(
        @Path("id") id: Int
    ): LiveData<ApiResponse<TrailerResponse>>

    @GET("movie/{id}/reviews")
    fun fetchMovieReview(
        @Path("id") id: Int
    ): LiveData<ApiResponse<ReviewsResponse>>
}