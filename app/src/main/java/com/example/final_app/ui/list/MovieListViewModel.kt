package com.example.final_app.ui.list

import androidx.lifecycle.*
import com.example.final_app.data.model.Movie
import com.example.final_app.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadMovies() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _movies.value = repository.getMovies()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load movies"
            }
            _loading.value = false
        }
    }
}
