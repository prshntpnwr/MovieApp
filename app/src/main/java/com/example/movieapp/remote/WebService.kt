package com.example.movieapp.remote

import androidx.lifecycle.LiveData
import com.example.movieapp.database.MoviesResponse
import com.example.movieapp.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET("discover/movie")
    fun getMovieList(@Query("api_key") api_key: String): LiveData<ApiResponse<MoviesResponse>>

}