package com.example.movieapp.util

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import retrofit2.Call

abstract class DataBoundResource<ResultType, RequestType> @MainThread constructor() {

    init {
        val local = loadFromDb()
        if (shouldFetch(local))
            fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall().execute()

        when {
            apiResponse.isSuccessful -> {
                apiResponse.body()?.let {
                    saveCallResult(it)
                }
            }

            else -> {
                val fail = Resource.error(
                    apiResponse.message() ?: "error not found",
                    apiResponse.body(),
                    apiResponse.code()
                )
                onFetchFailed(fail)
            }
        }
    }

    @MainThread
    protected abstract fun loadFromDb(): ResultType

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun createCall(): Call<RequestType>

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    protected  open fun onFetchFailed(item: Resource<RequestType>) {}
}