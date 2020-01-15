package com.example.movieapp.di.module

import android.annotation.SuppressLint
import android.util.Log
import com.example.movieapp.BuildConfig
import com.example.movieapp.remote.WebService
import com.example.movieapp.remote.WebServiceHolder
import com.example.movieapp.util.LiveDataCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    fun webServiceHolder(): WebServiceHolder {
        return WebServiceHolder.instance
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newUrl = chain
                .request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.ApiKey)
                .build()

            val newRequest = chain
                .request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }

    @SuppressLint("NewApi")
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        okHttpClient.sslSocketFactory()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiWebservice(restAdapter: Retrofit): WebService {
        val webService = restAdapter.create(WebService::class.java)
        WebServiceHolder.instance.setAPIService(webService)
        return webService
    }
}