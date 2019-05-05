package com.example.movieapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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

    private lateinit var viewModel: MovieDetailViewModel

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var binding: MovieDetailFragmentBinding

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOVIE_ID, movieId)
        outState.putString(MOVIE_TITLE, movieTitle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<MovieDetailFragmentBinding>(
            inflater,
            R.layout.movie_detail_fragment,
            container,
            false,
            dataBindingComponent
        ).also {
            binding = it
            binding.converter = ConverterUtil()
            binding.lifecycleOwner = this
        }.run {
            return this.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movieId = savedInstanceState?.getInt(MOVIE_ID) ?: MovieDetailFragmentArgs.fromBundle(arguments).id
        movieTitle = savedInstanceState?.getString(MOVIE_TITLE) ?: MovieDetailFragmentArgs.fromBundle(arguments).title

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MovieDetailViewModel::class.java)
            .also {
                it.init(refID = movieId)
                it.result.observe(this, Observer { res ->
                    binding.status = res.status
                    binding.item = res?.data
                    setActionBar()
                    if (res.status == Status.SUCCESS && res?.data == null)
                        binding.status = Status.ERROR

                    if (res?.status == Status.ERROR)
                        Toast.makeText(requireContext(), getString(R.string.generalError), Toast.LENGTH_LONG).show()
                })
            }
    }

    private fun setActionBar() {
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.let {
            it.title = movieTitle ?: getString(R.string.details)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
        }
        return true
    }

    companion object {
        var movieId: Int = -1
        var movieTitle: String? = null
        const val MOVIE_ID = "movie_id"
        const val MOVIE_TITLE = "movie_title"
    }
}
