package com.example.movieapp.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.movieapp.R
import com.example.movieapp.binding.FragmentDataBindingComponent
import com.example.movieapp.database.Movie
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.di.Injectable
import com.example.movieapp.observer.MoviesViewModel
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.Status
import com.google.gson.Gson
import javax.inject.Inject

class MoviesFragment : Fragment(), Injectable {

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MoviesViewModel

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var binding: MoviesFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<MoviesFragmentBinding>(
            inflater,
            R.layout.movies_fragment,
            container,
            false,
            dataBindingComponent
        ).also {
            binding = it
        }.run {
            return this.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setActionBar()

        val adapter = MoviesAdapter(dataBindingComponent, executors, this::onItemClick)
        binding.let {
            it.rvMovies.adapter = adapter
            it.lifecycleOwner = this
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MoviesViewModel::class.java)
            .also {
                it.fetchTask(FILTER_TRENDY)
                it.result.observe(this, Observer { res ->
                    binding.status = res.status
                    adapter.submitList(res?.data)
                    it.updateFetch(flag = false)
                    when (res?.status) {
                        Status.SUCCESS -> {
                            if (res.data?.size == 0)
                                binding.status = Status.ERROR
                        }

                        Status.ERROR -> {
                            Toast.makeText(requireContext(), res.message, Toast.LENGTH_LONG).show()
                        }

                        Status.LOADING -> { }
                    }
                })
            }
    }

    private fun onItemClick(movie: Movie) {
        val action = MoviesFragmentDirections.actionMovieFragmentToMovieDetailFragment(movie.id, movie.title)
        navController().navigate(action)
    }

    private fun setActionBar() {
        (activity as AppCompatActivity).supportActionBar.let { actionBar ->
            actionBar?.let {
                it.title = getString(R.string.app_name)
                it.setDisplayHomeAsUpEnabled(false)
                it.setDisplayShowHomeEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFetch(flag = true)
        when (item.itemId) {
            R.id.action_trendy -> {
                viewModel.fetchTask(FILTER_TRENDY)
            }
            R.id.action_rated -> {
                viewModel.fetchTask(FILTER_RATED)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navController() = findNavController()

    companion object {
        const val FILTER_TRENDY = 0
        const val FILTER_RATED = 1
    }
}
