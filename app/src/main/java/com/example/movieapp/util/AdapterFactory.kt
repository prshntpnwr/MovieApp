package com.example.movieapp.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

abstract class AdapterFactory<T, R> {
    private val result = MediatorLiveData<T>()
    private val response = MutableLiveData<R>()

    init {
        Log.e(javaClass.name, "AdapterFactory1")
        result.addSource(dataSource()) {
            Log.e(javaClass.name, "AdapterFactory2")
            onSourceChange(it)
        }
    }

    private fun onSourceChange(data: T) {
        val res = adapt(data)
        Log.e(javaClass.name, "adapter_factory_source: ${Gson().toJson(res)}")
        response.value = res
    }

    abstract fun adapt(data: T): R

    abstract fun dataSource(): LiveData<T>

    fun asLiveData(): LiveData<R> {
        return response
    }
}