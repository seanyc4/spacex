package com.seancoyle.movies.framework.presentation.movielist.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.databinding.RvMovieListBinding
import com.seancoyle.movies.framework.presentation.movielist.glideLoadMoviePosters

class MovieListAdapter
constructor(
    private val interaction: Interaction? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var moviesList: List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MovieListViewHolder(
            RvMovieListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieListViewHolder -> {
                holder.bind(moviesList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (!moviesList.isNullOrEmpty()) {
            moviesList.size
        } else 0
    }

    fun updateAdapter(movies: List<Movie>) {
        moviesList = movies
        notifyDataSetChanged()
    }

    class MovieListViewHolder
    constructor(
        private val binding: RvMovieListBinding,
        private val interaction: Interaction?,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(itemView) {
            with(binding){

                movieListPoster.glideLoadMoviePosters(movie.poster_path, true)

            }

            setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, movie)
            }

        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, selectedMovie: Movie)

    }

}













