package com.example.movieapp.util

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showToast(message: String) = Toast.makeText(
    requireContext(),
    message,
    Toast.LENGTH_LONG
).show()

fun Fragment.showSnackBar(message: String) = Snackbar.make(
    this.requireView(),
    message,
    Snackbar.LENGTH_LONG
).show()

fun Fragment.setActionBar(title: String) {
    (activity as AppCompatActivity).supportActionBar?.apply {
        this.title = title
        setDisplayHomeAsUpEnabled(false)
        setDisplayShowHomeEnabled(false)
    }
}