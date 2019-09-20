package com.example.movieapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.R
import com.example.movieapp.database.Movie
import com.example.movieapp.databinding.ListItemMoviesBinding
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.DataBoundListAdapter

class MoviesAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Movie, ImageView) -> Unit)?
) : DataBoundListAdapter<Movie, ListItemMoviesBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListItemMoviesBinding {
        DataBindingUtil
            .inflate<ListItemMoviesBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_movies,
                parent,
                false,
                dataBindingComponent
            ).also { b ->
                b.root.setOnClickListener {
                    b.item?.let {
                        callback?.invoke(it, b.posterImage)
                    }
                }
            }.run {
                return this
            }
    }

    override fun bind(binding: ListItemMoviesBinding, item: Movie) {
        binding.item = item
    }
}