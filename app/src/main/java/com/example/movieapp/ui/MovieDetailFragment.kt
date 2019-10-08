package com.example.movieapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.example.movieapp.R
import com.example.movieapp.binding.FragmentDataBindingComponent
import com.example.movieapp.database.MovieDetail
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.di.Injectable
import com.example.movieapp.observer.MovieDetailViewModel
import com.example.movieapp.util.*
import com.google.gson.Gson
import javax.inject.Inject

class MovieDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MovieDetailViewModel by viewModels { viewModelFactory }
    private val dataBindingComponent: DataBindingComponent by lazy {
        FragmentDataBindingComponent(
            this
        )
    }
    private val trailerAdapter by lazy {
        TrailersAdapter(
            dataBindingComponent,
            executors
        ) { actionViewIntent(it.getTrailerUri()) }
    }
    private val reviewAdapter by lazy { ReviewAdapter(dataBindingComponent, executors, null) }
    private val castAdapter by lazy { CastAdapter(dataBindingComponent, executors, null) }
    private lateinit var binding: MovieDetailFragmentBinding

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOVIE_ID, movieId)
        outState.putString(MOVIE_TITLE, movieTitle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val transition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = ChangeBounds().apply { enterTransition = transition }
        sharedElementReturnTransition = ChangeBounds().apply { returnTransition = transition }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<MovieDetailFragmentBinding>(
            inflater,
            R.layout.movie_detail_fragment,
            container,
            false,
            dataBindingComponent
        ).apply {
            lifecycleOwner = this@MovieDetailFragment
            viewPagerTrailer.adapter = trailerAdapter
            rvReview.adapter = reviewAdapter
            rvCast.adapter = castAdapter
            binding = this
        }.run {
            return this.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieId = savedInstanceState?.getInt(MOVIE_ID) ?: arguments?.getInt(MOVIE_ID) ?: 0
        movieTitle = savedInstanceState?.getString(MOVIE_TITLE) ?: arguments?.getString(MOVIE_TITLE)
        setActionBar(movieTitle ?: getString(R.string.details))
        viewModel.apply {
            init(refID = movieId)
            binding.viewModel = this
            result.observe(this@MovieDetailFragment, Observer { res ->
                Log.e(
                    Thread.currentThread().name,
                    "details: $movieId ${Gson().toJson(res?.data?.cast)}"
                )
                notifyViews()
                trailerAdapter.submitList(res?.data?.trailers)
                reviewAdapter.submitList(res?.data?.reviews)
                castAdapter.submitList(res?.data?.cast)
                if (res?.status == Status.ERROR)
                    showToast(getString(R.string.generalError))
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        var movieId: Int = -1
        var movieTitle: String? = null
        const val MOVIE_ID = "movie_id"
        const val MOVIE_TITLE = "movie_title"

        const val CALL_DETAIL = 0
        const val CALL_TRAILER = 1
    }
}
