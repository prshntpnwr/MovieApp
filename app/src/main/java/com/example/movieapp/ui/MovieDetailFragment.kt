package com.example.movieapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.movieapp.R
import com.example.movieapp.binding.FragmentDataBindingComponent
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.di.Injectable
import com.example.movieapp.observer.MovieDetailViewModel
import com.example.movieapp.util.AppExecutors
import com.example.movieapp.util.ConverterUtil
import com.example.movieapp.util.Resource
import com.example.movieapp.util.Status
import javax.inject.Inject

class MovieDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MovieDetailViewModel by viewModels { viewModelFactory }
    private val dataBindingComponent: DataBindingComponent by lazy { FragmentDataBindingComponent(this) }
    private lateinit var binding: MovieDetailFragmentBinding

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOVIE_ID, movieId)
        outState.putString(MOVIE_TITLE, movieTitle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
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
        ).also {
            it.converter = ConverterUtil()
            it.lifecycleOwner = this
            binding = it
            return binding.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieId = savedInstanceState?.getInt(MOVIE_ID) ?: arguments?.getInt(MOVIE_ID) ?: 0
        movieTitle = savedInstanceState?.getString(MOVIE_TITLE) ?: arguments?.getString(MOVIE_TITLE)

        viewModel.also {
            it.init(refID = movieId)
            it.result.observe(this, Observer { res ->
                binding.status = res.status
                binding.item = res?.data
                setActionBar()
                if (res.status == Status.SUCCESS && res?.data == null)
                    binding.status = Status.ERROR

                if (res?.status == Status.ERROR)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.generalError),
                        Toast.LENGTH_LONG
                    ).show()
            })
        }
    }

    private fun setActionBar() {
        (activity as MainActivity).supportActionBar?.apply {
            title = movieTitle ?: getString(R.string.details)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        var movieId: Int = -1
        var movieTitle: String? = null
        const val MOVIE_ID = "movie_id"
        const val MOVIE_TITLE = "movie_title"
    }
}
