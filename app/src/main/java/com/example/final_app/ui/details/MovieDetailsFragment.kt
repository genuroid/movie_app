package com.example.final_app.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import coil.load
import com.example.final_app.R
import com.example.final_app.databinding.FragmentMovieDetailsBinding
import androidx.navigation.fragment.navArgs
import android.graphics.Color
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

        binding.textTitle.text = args.title
        binding.textOverview.text = args.overview
        binding.imagePoster.load(args.posterPath)

        requireActivity().title = args.title

        val scorePercentage = (args.rating * 10).toInt().coerceIn(0, 100)

        val level = scorePercentage * 100
        binding.ratingCircle.drawable.level = level

        val color = when {
            scorePercentage >= 80 -> Color.parseColor("#4CAF50")
            scorePercentage >= 50 -> Color.parseColor("#FFC107")
            else -> Color.parseColor("#F44336")
        }

        binding.ratingCircle.drawable.mutate().setTint(color)
        binding.detailRatingText.text = "$scorePercentage%"


    }

}