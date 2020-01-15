package com.example.movieapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.binding.FragmentDataBindingComponent
import com.example.movieapp.database.Movie
import com.example.movieapp.database.PreferenceManager
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.di.Injectable
import com.example.movieapp.observer.MoviesViewModel
import com.example.movieapp.ui.MovieDetailFragment.Companion.MOVIE_ID
import com.example.movieapp.ui.MovieDetailFragment.Companion.MOVIE_TITLE
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.Status
import com.example.movieapp.util.setActionBar
import com.example.movieapp.util.showSnackBar
import com.google.gson.Gson
import javax.inject.Inject

class MoviesFragment : Fragment(), Injectable {

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var prefManager: PreferenceManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels { viewModelFactory }
    private val dataBindingComponent: DataBindingComponent by lazy { FragmentDataBindingComponent(this) }
    private val adapter by lazy { MoviesAdapter(dataBindingComponent, executors, ::onItemClick) }

    private lateinit var binding: MoviesFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<MoviesFragmentBinding>(
            inflater,
            R.layout.movies_fragment,
            container,
            false,
            dataBindingComponent
        ).also {
            it.viewModel = viewModel
            it.rvMovies.adapter = adapter
            it.lifecycleOwner = this
            binding = it
            (activity as AppCompatActivity).supportActionBar?.show()
        }.run {
            return this.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setActionBar(getString(R.string.app_name))
//        applyTheme()

        viewModel.apply {
            loadNow()
            result.observe(this@MoviesFragment, Observer { res ->
                adapter.submitList(res?.data)
                Log.e(Thread.currentThread().name, "movies: ${Gson().toJson(res)}")
                if (res?.status == Status.ERROR) showSnackBar(res.message ?: "")
            })
        }
    }

    @SuppressLint("NewApi")
    private fun onItemClick(movie: Movie, view: ImageView) {
        val extras = FragmentNavigatorExtras(
            view to view.transitionName
        )

        val bundle = bundleOf(
            MOVIE_ID to movie.id,
            MOVIE_TITLE to movie.title
        )

        navController().navigate(
            R.id.action_movieFragment_to_movieDetailFragment,
            bundle,
            null,
            extras
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        viewModel.updateFetch(flag = true)
        when (item.itemId) {
            R.id.action_theme -> changeTheme()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeTheme() {
        val theme = when(viewModel.currentTheme ) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                viewModel.currentTheme = AppCompatDelegate.MODE_NIGHT_YES
                AppCompatDelegate.MODE_NIGHT_YES
            } else -> {
                viewModel.currentTheme = AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.MODE_NIGHT_NO
            }
        }
        setDefaultNightMode(theme)
    }

    private fun navController() = findNavController()

    companion object {
        const val FILTER_TRENDY = 0
        const val FILTER_RATED = 1
        const val FILTER_LATEST = 2
        const val FILTER_UPCOMING = 3
        const val FILTER_PLAYING_NOW = 4
    }
}