package com.example.final_app.ui.list

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_app.R
import com.example.final_app.adapter.MovieAdapter
import com.example.final_app.databinding.FragmentMovieListBinding

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchBar()
        setupSortMenu()

        // Initialize Adapter
        movieAdapter = MovieAdapter(emptyList()) { movie ->
            val action = MovieListFragmentDirections
                .actionMovieListFragmentToMovieDetailsFragment(
                    title = movie.title,
                    overview = movie.overview,
                    posterPath = movie.posterUrl,
                    rating = movie.rating.toFloat()
                )
            findNavController().navigate(action)
        }

        movieAdapter.onResultsChanged = { isEmpty ->
            updateEmptyState(isEmpty)
        }

        // Setup RecyclerView
        binding.recyclerViewMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieAdapter
        }

        // Swipe Refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadMovies()
        }

        // Observe Data
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            movieAdapter.updateMovies(movies)
            if (viewModel.loading.value == false && viewModel.error.value == null) {
                updateEmptyState(movies.isEmpty())
            }
        }

        // Observe Loading State
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe Error State
        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.textError.visibility = if (error != null) View.VISIBLE else View.GONE
        }

        if (viewModel.movies.value.isNullOrEmpty()) {
            viewModel.loadMovies()
        }
    }

    private fun setupSearchBar() {
        // 1. Link the Bar to the View (This makes the "expand" animation work)
        binding.searchView.setupWithSearchBar(binding.searchBar)

        // 2. Handle the typing logic
        binding.searchView.editText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter your adapter as the user types
                movieAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // 3. Optional: Clear filter when search view closes
        binding.searchView.addTransitionListener { _, _, newState ->
            if (newState == com.google.android.material.search.SearchView.TransitionState.HIDDEN) {
                movieAdapter.filter("")
            }
        }
    }

    private fun setupSortMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_movie_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return handleSort(menuItem.itemId)
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun handleSort(itemId: Int): Boolean {
        return when (itemId) {
            R.id.action_sort_az -> {
                movieAdapter.sortAZ()
                true
            }
            R.id.action_sort_za -> {
                movieAdapter.sortZA()
                true
            }
            R.id.action_sort_rating -> {
                movieAdapter.sortByRating() // You added this to your Adapter earlier!
                true
            }
            else -> false
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.recyclerViewMovies.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.textEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}