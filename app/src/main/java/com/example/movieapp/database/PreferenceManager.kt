package com.example.movieapp.database

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton

@Singleton
class PreferenceManager {

    companion object {
        private var mSharedPreferences: SharedPreferences? = null
        private const val PREF_NAME = "app_shared_preferences"
        private var mInstance: PreferenceManager = PreferenceManager()

        fun getInstance(context: Context): PreferenceManager {
            if (mSharedPreferences == null)
                mSharedPreferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
            return mInstance
        }
    }

    enum class Key {
        THEME
    }

    fun getInt(key: Key, default: Int): Int {
        return mSharedPreferences?.getInt(key.name, default) ?: default
    }

    fun put(key: Key, value: Int) {
        mSharedPreferences?.let { pref ->
            val editor = pref.edit()
            editor.putInt(key.name, value)
            editor.commit()
        }
    }

}