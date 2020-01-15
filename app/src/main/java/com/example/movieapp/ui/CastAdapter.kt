package com.example.movieapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.R
import com.example.movieapp.database.Cast
import com.example.movieapp.databinding.ListItemCastBinding
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.DataBoundListAdapter

class CastAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Cast, ImageView) -> Unit)?
) : DataBoundListAdapter<Cast, ListItemCastBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListItemCastBinding {
        DataBindingUtil
            .inflate<ListItemCastBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_cast,
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

    override fun bind(binding: ListItemCastBinding, item: Cast) {
        binding.item = item
    }
}