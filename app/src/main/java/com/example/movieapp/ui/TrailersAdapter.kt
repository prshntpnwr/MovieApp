package com.example.movieapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.R
import com.example.movieapp.database.MovieTrailer
import com.example.movieapp.databinding.ListItemTrailerBinding
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.DataBoundListAdapter

class TrailersAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((MovieTrailer) -> Unit)?
) : DataBoundListAdapter<MovieTrailer, ListItemTrailerBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<MovieTrailer>() {
        override fun areItemsTheSame(oldItem: MovieTrailer, newItem: MovieTrailer): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieTrailer, newItem: MovieTrailer): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListItemTrailerBinding {
        DataBindingUtil
            .inflate<ListItemTrailerBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_trailer,
                parent,
                false,
                dataBindingComponent
            ).also { b ->
                b.root.setOnClickListener {
                    b.item?.let {
                        callback?.invoke(it)
                    }
                }
            }.run {
                return this
            }
    }

    override fun bind(binding: ListItemTrailerBinding, item: MovieTrailer) {
        binding.item = item
        Log.e(this.javaClass.name, "url_is: ${item.getTrailerPreview()}")
    }
}