package usth.ict.group20.imdb

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import usth.ict.group20.imdb.adapters.FilmAdapter
import usth.ict.group20.imdb.models.Film

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var noResultsTextView: TextView
    private lateinit var resultsInfoTextView: TextView
    private lateinit var filmAdapter: FilmAdapter

    // This list would ideally come from a shared repository or database
    private val allFilms = mutableListOf<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        // Setup Toolbar with a back button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Search Results"

        // Initialize views
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview)
        noResultsTextView = findViewById(R.id.no_results_textview)
        resultsInfoTextView = findViewById(R.id.results_info_textview)

        // Setup RecyclerView
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Populate the master list of films (same as in MainActivity)
        setupAllFilmData()

        // Get the search query from the intent
        val query = intent.getStringExtra("SEARCH_QUERY")

        if (query.isNullOrBlank()) {
            displayNoResults("No query provided.")
        } else {
            resultsInfoTextView.text = "Showing results for \"$query\""
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        // Filter the master list of films based on the query
        val filteredList = allFilms.filter { film ->
            film.title.contains(query, ignoreCase = true)
        }

        if (filteredList.isEmpty()) {
            displayNoResults()
        } else {
            displayResults(filteredList)
        }
    }

    private fun displayResults(results: List<Film>) {
        filmAdapter = FilmAdapter(results)
        searchResultsRecyclerView.adapter = filmAdapter
        searchResultsRecyclerView.visibility = View.VISIBLE
        noResultsTextView.visibility = View.GONE
    }

    private fun displayNoResults(message: String = "No results found.") {
        noResultsTextView.text = message
        searchResultsRecyclerView.visibility = View.GONE
        noResultsTextView.visibility = View.VISIBLE
    }

    // This handles the action of the back button in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Helper function to populate the film list.
    // In a real app, this data would come from a single source like a database or a network call.
    private fun setupAllFilmData() {
        allFilms.addAll(
            listOf(
                Film(1, "The Shawshank Redemption", R.drawable.shawshank, 9.3, 1994, "R"),
                Film(2, "The Godfather", R.drawable.the_godfather, 9.2, 1972, "R"),
                Film(3, "The Dark Knight", R.drawable.the_dark_knight, 9.0, 2008, "PG-13"),
                Film(4, "Pulp Fiction", R.drawable.pulp, 8.9, 1994, "R"),
                Film(10, "Interstellar", R.drawable.interstellar, 8.7, 2014, "PG-13"),
                Film(11, "The Matrix", R.drawable.the_matrix, 8.7, 1999, "R"),
                Film(12, "Goodfellas", R.drawable.goodfellas, 8.7, 1990, "R"),
                Film(20, "Dune: Part Two", R.drawable.dune2, 8.8, 2024, "PG-13"),
                Film(21, "Kung Fu Panda 4", R.drawable.kungfu4, 6.8, 2024, "PG"),
                Film(30, "Back to the Future", R.drawable.back_to_the_future, 8.5, 1985, "PG"),
                Film(31, "Jurassic Park", R.drawable.jurassic_park, 8.2, 1993, "PG-13"),
                Film(40, "Oppenheimer", R.drawable.oppenheimer, 8.6, 2023, "R"),
                Film(41, "Parasite", R.drawable.parasite, 8.5, 2019, "R"),
                Film(50, "Glass Onion", R.drawable.shawshank, 7.1, 2022, "PG-13"),
                Film(60, "Avatar: The Way of Water", R.drawable.shawshank, 7.6, 2022, "PG-13")
            )
        )
    }
}