package com.example.final_app.data.repository

import com.example.final_app.data.model.Movie
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MovieRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getMovies(): List<Movie> {
        return firestore.collection("movies")
            .get()
            .await()
            .toObjects(Movie::class.java)
    }
}

