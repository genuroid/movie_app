package com.example.final_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.final_app.R
import com.example.final_app.data.model.Movie
import com.example.final_app.databinding.ItemMovieBinding

class MovieAdapter(
    private var movies: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>()
{

    var onResultsChanged: ((Boolean) -> Unit)? = null
    private var allMovies: List<Movie> = movies.toList()

    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        // movie_title in XML becomes movieTitle in ViewBinding
        holder.binding.movieTitle.text = movie.title

        holder.binding.movieRating.text = if (movie.rating > 0) movie.rating.toString() else "-"
        // movie_poster in XML becomes moviePoster in ViewBinding
        holder.binding.moviePoster.load(movie.posterUrl) {
            crossfade(true) // Professional touch: smooth fade-in
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_placeholder)
        }

        holder.binding.root.setOnClickListener {
            onClick(movie)
        }
        holder.binding.root.alpha = 0f
        holder.binding.root.animate().alpha(1f).setDuration(400).setStartDelay(position * 50L).start()



    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        allMovies = newMovies.toList()
        movies = newMovies.toList()
        notifyDataSetChanged()
        onResultsChanged?.invoke(movies.isEmpty())
    }

    fun filter(query: String) {
        movies = if (query.isBlank()) {
            allMovies
        } else {
            allMovies.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
        onResultsChanged?.invoke(movies.isEmpty())
    }

    fun sortAZ() {
        movies = movies.sortedBy { it.title.lowercase() }
        notifyDataSetChanged()
    }

    fun sortZA() {
        movies = movies.sortedByDescending { it.title.lowercase() }
        notifyDataSetChanged()
    }

    fun sortByRating() {
        movies = movies.sortedByDescending { it.rating }
        notifyDataSetChanged()
    }


}

