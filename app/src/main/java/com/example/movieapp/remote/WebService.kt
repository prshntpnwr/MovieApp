package com.example.movieapp.remote

import androidx.lifecycle.LiveData
import com.example.movieapp.database.MovieDetail
import com.example.movieapp.database.MoviesResponse
import com.example.movieapp.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.HashMap

interface WebService {

    @GET("discover/movie")
    fun getMovieList(@Query("api_key") apiKey: String,
                     @QueryMap sortBy: HashMap<String, String>
    ): LiveData<ApiResponse<MoviesResponse>>

    @GET("movie/{id}")
    fun fetchMovieDetails(@Path("id") id: Int,
                          @Query("api_key") apiKey: String
    ) : LiveData<ApiResponse<MovieDetail>>
}