package com.example.movieapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.R
import com.example.movieapp.database.Reviews
import com.example.movieapp.databinding.ListItemReviewBinding
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.DataBoundListAdapter

class ReviewAdapter (private val dataBindingComponent: DataBindingComponent,
                     appExecutors: AppExecutors,
                     private val callback: ((Reviews) -> Unit)?
) : DataBoundListAdapter<Reviews, ListItemReviewBinding>(
appExecutors = appExecutors,
diffCallback = object : DiffUtil.ItemCallback<Reviews>() {
    override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
        return oldItem.id == newItem.id
    }
}) {

    override fun createBinding(parent: ViewGroup): ListItemReviewBinding {
        DataBindingUtil
            .inflate<ListItemReviewBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_review,
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

    override fun bind(binding: ListItemReviewBinding, item: Reviews) {
        binding.item = item
    }
}