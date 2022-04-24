package com.seancoyle.movies.framework.presentation.moviedetail.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.databinding.RvMovieCastBinding
import com.seancoyle.movies.framework.presentation.moviedetail.glideLoadMovieCast

class MovieCastAdapter
constructor(
    private val interaction: Interaction? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var movieCast: List<Cast> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MovieCastViewHolder(
            RvMovieCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction = interaction
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieCastViewHolder -> {
                holder.bind(movieCast[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (!movieCast.isNullOrEmpty()) {
            movieCast.size
        } else 0
    }

    fun updateAdapter(movieCast: List<Cast>) {
        this.movieCast = movieCast
        notifyDataSetChanged()
    }

    class MovieCastViewHolder
    constructor(
        private val binding: RvMovieCastBinding,
        private val interaction: Interaction?,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Cast) = with(itemView) {
            with(binding){

                castImage.glideLoadMovieCast(item.profile_path, true)
                name.text = item.name

            }

          /*  setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }*/

        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Cast)

    }

}













