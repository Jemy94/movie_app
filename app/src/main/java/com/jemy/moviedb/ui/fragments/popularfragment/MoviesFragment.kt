package com.jemy.moviedb.ui.fragments.popularfragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jemy.moviedb.R
import com.jemy.moviedb.data.common.ResourceState.*
import com.jemy.moviedb.data.response.PopularMoviesResponse.Movie
import com.jemy.moviedb.databinding.FragmentMoviesBinding
import com.jemy.moviedb.ui.fragments.popularfragment.adapter.MoviesAdapter
import com.jemy.moviedb.utils.Constants.Error.GENERAL
import com.jemy.moviedb.utils.extensions.load
import com.jemy.moviedb.utils.extensions.toastLong
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val viewModel: MoviesViewModel by viewModels()
    private var binding: FragmentMoviesBinding? = null
    private val adapter by lazy { MoviesAdapter() }
    private val detailsDialog: Dialog by lazy { Dialog(requireActivity(),R.style.DialogTheme) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setupViewBinding(inflater, container)

    private fun setupViewBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instantiateSingleAndMultiDialog()
        getPopular()
        observePopular()
    }

    private fun instantiateSingleAndMultiDialog() {
        detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        detailsDialog.setContentView(R.layout.dialog_movie_details)
    }

    private fun getPopular() {
        viewModel.getPopular()
    }

    private fun observePopular() {
        viewModel.popular.observe(viewLifecycleOwner, { resource ->
            when (resource.state) {
                LOADING -> binding?.popularProgressBar?.visibility = View.VISIBLE
                SUCCESS -> {
                    binding?.popularProgressBar?.visibility = View.GONE
                    resource.data?.let { response ->
                        response.results?.let { setupPopularAdapter(it) }
                    }
                }
                ERROR -> {
                    binding?.popularProgressBar?.visibility = View.GONE
                    resource.message?.let { msg ->
                        when (msg) {
                            GENERAL -> requireActivity().toastLong(getString(R.string.general_error))
                            else -> {
                                requireActivity().toastLong(msg)
                            }
                        }
                    } ?: requireActivity().toastLong(getString(R.string.general_error))
                }
            }
        })
    }

    private fun setupPopularAdapter(list: List<Movie>) {
        adapter.addItems(list)
        binding?.moviesRecycler?.adapter = adapter
        adapter.setItemCallBack { movie ->
            showMovieDetailsDialog(movie!!)
        }
    }

    private fun showMovieDetailsDialog(movie: Movie) {
        detailsDialog.setCanceledOnTouchOutside(true)
        val popularity = detailsDialog.findViewById<TextView>(R.id.popularity)
        val title = detailsDialog.findViewById<TextView>(R.id.movieTitle)
        val description = detailsDialog.findViewById<TextView>(R.id.movieDescription)
        val image = detailsDialog.findViewById<ImageView>(R.id.movieImageView)
        popularity.text = movie.popularity.toString()
        title.text = movie.originalTitle
        description.text = movie.overview
        movie.posterPath?.let { image.load(it) }
        detailsDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val metrics = requireActivity().resources.displayMetrics
        detailsDialog.window?.setLayout(
            (metrics.widthPixels * 0.8f).toInt(),
            (metrics.heightPixels * 0.8f).toInt()
        )
        detailsDialog.show()
    }

}