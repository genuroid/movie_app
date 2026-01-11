package com.example.final_app.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    val movies: List<Movie> // This maps the "results" array from the API to your Movie list
)