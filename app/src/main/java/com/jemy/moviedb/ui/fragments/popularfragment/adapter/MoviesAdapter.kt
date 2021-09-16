package com.jemy.moviedb.ui.fragments.popularfragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jemy.moviedb.data.response.PopularMoviesResponse.Movie
import com.jemy.moviedb.databinding.ItemMovieBinding
import com.jemy.moviedb.utils.extensions.load

class MoviesAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private var itemCallback: ((Movie?) -> Unit)? = null
   private var items = mutableListOf<Movie>()

    fun addItems(items: List<Movie>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding, itemCallback)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val user = items[position]
        holder.bind(user)
    }

    fun setItemCallBack(itemCallback: (Movie?) -> Unit) {
        this.itemCallback = itemCallback
    }

    override fun getItemCount(): Int = items.size
}

class MovieViewHolder(
    itemUserBinding: ItemMovieBinding,
    private val itemCallback: ((Movie?) -> Unit)?
) : RecyclerView.ViewHolder(itemUserBinding.root) {

    private var itemView = itemUserBinding.itemView
    private var title = itemUserBinding.movieTitle
    private var popularity = itemUserBinding.moviePopularity
    private var image = itemUserBinding.movieImage

    fun bind(movie: Movie?) {
        itemView.setOnClickListener { itemCallback?.invoke(movie) }
        title.text = movie?.originalTitle
        popularity.text = movie?.popularity.toString()
        movie?.posterPath?.let { image.load(it) }
    }
}