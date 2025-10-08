package usth.ict.group20.imdb

import android.content.Intent
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.BaseColumns
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usth.ict.group20.imdb.adapters.CarouselAdapter
import usth.ict.group20.imdb.adapters.CarouselItem
import usth.ict.group20.imdb.adapters.CelebrityAdapter
import usth.ict.group20.imdb.adapters.FilmAdapter
import usth.ict.group20.imdb.adapters.HotSearchAdapter
import usth.ict.group20.imdb.adapters.NewsArticleAdapter
import usth.ict.group20.imdb.models.Celebrity
import usth.ict.group20.imdb.models.Film
import usth.ict.group20.imdb.models.NewsArticle

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val scrollHandler = Handler(Looper.getMainLooper())
    private lateinit var scrollRunnable: Runnable

    // Make allFilms a reactive data source
    private val _allFilms = MutableStateFlow<List<Film>>(emptyList())
    private val allFilms = _allFilms.asStateFlow()

    private lateinit var searchView: SearchView
    private lateinit var suggestionAdapter: SimpleCursorAdapter

    // Adapters for your RecyclerViews
    private lateinit var top10Adapter: FilmAdapter
    private lateinit var topPicksAdapter: FilmAdapter
    private lateinit var inTheatersAdapter: FilmAdapter
    private lateinit var fanFavoritesAdapter: FilmAdapter
    private lateinit var oscarWinnersAdapter: FilmAdapter
    private lateinit var streamingNowAdapter: FilmAdapter
    private lateinit var topBoxOfficeAdapter: FilmAdapter
    private lateinit var comingSoonAdapter: FilmAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize adapters
        top10Adapter = FilmAdapter(emptyList())
        topPicksAdapter = FilmAdapter(emptyList())
        inTheatersAdapter = FilmAdapter(emptyList())
        fanFavoritesAdapter = FilmAdapter(emptyList())
        oscarWinnersAdapter = FilmAdapter(emptyList())
        streamingNowAdapter = FilmAdapter(emptyList())
        topBoxOfficeAdapter = FilmAdapter(emptyList())
        comingSoonAdapter = FilmAdapter(emptyList())

        // Setup UI components
        setupSearchSuggestions()
        setupSearch()
        setupFeaturedCarousel()
        setupHotSearch()
        setupFeaturedToday()
        setupWhatToWatchLists()
        setupBornToday()
        setupExploreMoviesLists()
        setupMoreToExplore()

        // Load initial data
        loadInitialFilmData()

        // Observe changes to the film list and update UI accordingly
        lifecycleScope.launch {
            allFilms.collect { films ->
                updateAllLists(films)
            }
        }
    }

    private fun loadInitialFilmData() {
        _allFilms.update {
            listOf(
                Film(1, "The Shawshank Redemption", R.drawable.shawshank, 9.3, 1994, "R"),
                Film(2, "The Godfather", R.drawable.the_godfather, 9.2, 1972, "R"),
                Film(3, "The Dark Knight", R.drawable.the_dark_knight, 9.0, 2008, "PG-13"),
                Film(4, "Pulp Fiction", R.drawable.pulp, 8.9, 1994, "R"),
                Film(5, "Breaking Bad", R.drawable.breaking_bad, 9.0, 2023, "TV-14"),
                Film(6, "Game of Thrones", R.drawable.game_of_thrones, 9.2, 2011, "TV-MA"),
                Film(7, "Stranger Things", R.drawable.stranger_things, 8.7, 2016, "TV-14"),
                Film(8, "The Office", R.drawable.the_office, 9.0, 2005, "TV-14"),
                Film(9, "The Walking Dead", R.drawable.walking_dead, 8.0, 2010, "TV-14"),
                Film(10, "The Boys", R.drawable.the_boys, 8.3, 2019, "TV-14"),
                Film(11, "Interstellar", R.drawable.interstellar, 8.7, 2014, "PG-13"),
                Film(12, "The Matrix", R.drawable.the_matrix, 8.7, 1999, "R"),
                Film(13, "Goodfellas", R.drawable.goodfellas, 8.7, 1990, "R"),
                Film(20, "Dune: Part Two", R.drawable.dune2, 8.8, 2024, "PG-13"),
                Film(21, "Kung Fu Panda 4", R.drawable.kungfu4, 6.8, 2024, "PG"),
                Film(30, "Back to the Future", R.drawable.back_to_the_future, 8.5, 1985, "PG"),
                Film(31, "Jurassic Park", R.drawable.jurassic_park, 8.2, 1993, "PG-13"),
                Film(40, "Oppenheimer", R.drawable.oppenheimer, 8.6, 2023, "R"),
                Film(41, "Parasite", R.drawable.parasite, 8.5, 2019, "R"),
                Film(50, "Glass Onion", R.drawable.glass_onion, 7.1, 2022, "PG-13"),
                Film(60, "Avatar: The Way of Water", R.drawable.avatar, 7.6, 2022, "PG-13")
            )
        }
    }

    /**
     * Call this method whenever you want to add a new film.
     * The UI will update automatically.
     */
    fun addFilm(newFilm: Film) {
        _allFilms.update { currentFilms ->
            // Add the new film, ensuring no duplicate IDs
            if (currentFilms.any { it.id == newFilm.id }) {
                // Optional: show a toast or log a warning if the ID already exists
                Toast.makeText(this, "Film with ID ${newFilm.id} already exists.", Toast.LENGTH_SHORT).show()
                currentFilms
            } else {
                currentFilms + newFilm
            }
        }
    }

    private fun updateAllLists(films: List<Film>) {
        // Update adapters with filtered lists
        top10Adapter.updateFilms(films.filter { it.id in 1..10 })
        topPicksAdapter.updateFilms(films.filter { it.id in 11..13 })
        inTheatersAdapter.updateFilms(films.filter { it.id in 20..29 })
        fanFavoritesAdapter.updateFilms(films.filter { it.id in 30..39 })
        oscarWinnersAdapter.updateFilms(films.filter { it.id in 40..49 })
        streamingNowAdapter.updateFilms(films.filter { it.id == 50 })
        topBoxOfficeAdapter.updateFilms(films.filter { it.id == 60 })
        comingSoonAdapter.updateFilms(films.filter { it.category == "Coming Soon" }) // Example category

        // This makes sure search suggestions are always up-to-date
        // No explicit action needed here, as onQueryTextChange already uses the latest list
    }

    // --- Existing Methods (with modifications) ---

    private fun setupSearch() {
        searchView = findViewById(R.id.home_search_view)
        searchView.suggestionsAdapter = suggestionAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    val intent = Intent(this@MainActivity, SearchResultsActivity::class.java).apply {
                        putExtra("SEARCH_QUERY", query)
                    }
                    startActivity(intent)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // *** MODIFICATION 1: Add a new column for the Film ID ***
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, "filmTitle", "filmId"))
                newText?.let { query ->
                    if (query.isNotBlank()) {
                        // Use the current value of the state flow for searching
                        allFilms.value.forEach { film ->
                            if (film.title.contains(query, ignoreCase = true)) {
                                // *** MODIFICATION 2: Add the film's ID to the cursor row ***
                                cursor.addRow(arrayOf(film.id, film.title, film.id))
                            }
                        }
                    }
                }
                suggestionAdapter.changeCursor(cursor)
                return true
            }
        })
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = suggestionAdapter.cursor
                if (cursor.moveToPosition(position)) {
                    // *** MODIFICATION 3: Get the film ID and title from the cursor ***
                    val filmId = cursor.getInt(cursor.getColumnIndexOrThrow("filmId"))
                    val filmTitle = cursor.getString(cursor.getColumnIndexOrThrow("filmTitle"))

                    // Clear focus and set the search text, but don't submit it
                    searchView.setQuery(filmTitle, false)
                    searchView.clearFocus()

                    // *** MODIFICATION 4: Directly start the activity with the specific film ID ***
                    val intent = Intent(this@MainActivity, SearchResultsActivity::class.java).apply {
                        // Pass both query and ID. The receiving activity can prioritize the ID.
                        putExtra("SEARCH_QUERY", filmTitle)
                        putExtra("FILM_ID", filmId)
                    }
                    startActivity(intent)
                }
                return true
            }
        })
    }

    private fun setupWhatToWatchLists() {
        // The lists are now initialized with adapters that will be updated automatically
        setupHorizontalRecyclerView(R.id.top_10_recyclerview, top10Adapter)
        setupHorizontalRecyclerView(R.id.top_picks_recyclerview, topPicksAdapter)
        setupHorizontalRecyclerView(R.id.in_theaters_recyclerview, inTheatersAdapter)
        setupHorizontalRecyclerView(R.id.fans_favorites_recyclerview, fanFavoritesAdapter)
        setupHorizontalRecyclerView(R.id.oscar_winners_recyclerview, oscarWinnersAdapter)
    }

    private fun setupExploreMoviesLists() {
        setupHorizontalRecyclerView(R.id.streaming_now_recyclerview, streamingNowAdapter)
        setupHorizontalRecyclerView(R.id.top_box_office_recyclerview, topBoxOfficeAdapter)
        setupHorizontalRecyclerView(R.id.coming_soon_recyclerview, comingSoonAdapter)
    }

    // --- No changes needed for the methods below ---

    private fun setupFeaturedCarousel() {
        viewPager = findViewById(R.id.featured_carousel_viewpager)
        val items = listOf(
            CarouselItem(R.drawable.oppenheimer), CarouselItem(R.drawable.dune2),
            CarouselItem(R.drawable.interstellar), CarouselItem(R.drawable.kungfu4),
            CarouselItem(R.drawable.back_to_the_future), CarouselItem(R.drawable.the_godfather),
            CarouselItem(R.drawable.the_matrix), CarouselItem(R.drawable.parasite),
            CarouselItem(R.drawable.shawshank), CarouselItem(R.drawable.the_dark_knight)
        )
        viewPager.adapter = CarouselAdapter(items)
        scrollRunnable = Runnable {
            var currentItem = viewPager.currentItem
            currentItem = if (currentItem == items.size - 1) 0 else currentItem + 1
            viewPager.setCurrentItem(currentItem, true)
        }
        startAutoScroll()
    }

    private fun startAutoScroll() {
        scrollHandler.postDelayed(scrollRunnable, 3000)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollHandler.removeCallbacks(scrollRunnable)
                scrollHandler.postDelayed(scrollRunnable, 3000)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        scrollHandler.removeCallbacks(scrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        scrollHandler.postDelayed(scrollRunnable, 3000)
    }

    private fun setupSearchSuggestions() {
        val from = arrayOf("filmTitle")
        val to = intArrayOf(android.R.id.text1)
        suggestionAdapter = SimpleCursorAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
    }

    private fun setupHorizontalRecyclerView(recyclerViewId: Int, adapter: RecyclerView.Adapter<*>) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun setupVerticalRecyclerView(recyclerViewId: Int, adapter: RecyclerView.Adapter<*>) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun setupHotSearch() {
        val hotSearches = listOf("The Marvels", "Oppenheimer", "Interstellar", "Trending Actors")
        val adapter = HotSearchAdapter(hotSearches) { query ->
            val intent = Intent(this, SearchResultsActivity::class.java).apply {
                putExtra("SEARCH_QUERY", query)
            }
            startActivity(intent)
        }
        setupHorizontalRecyclerView(R.id.hot_search_recyclerview, adapter)
    }

    private fun setupFeaturedToday() {
        val recyclerView = findViewById<RecyclerView>(R.id.featured_today_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.isNestedScrollingEnabled = false // Helps with smooth scrolling inside a ScrollView

        // Note: You would typically fetch this data from an API.
        // I am using placeholder data and image URLs.
        val newsArticles = listOf(
            NewsArticle(
                id = "1",
                headline = "Oscars 2024: The Biggest Snubs and Surprises",
                source = "Variety",
                timeAgo = "3h ago",
                summary = "The 96th Academy Awards saw 'Oppenheimer' dominate, but several fan-favorites were left empty-handed.",
                imageUrl = R.drawable.oscar
            ),
            NewsArticle(
                id = "2",
                headline = "Warner Bros. Shuffles Release Dates for 'The Batman 2'",
                source = "Deadline",
                timeAgo = "5h ago",
                summary = "Matt Reeves' 'The Batman Part II' has been pushed back a full year to October 2, 2026.",
                imageUrl = R.drawable.the_batman_2
            ),
            NewsArticle(
                id = "3",
                headline = "Dwayne Johnson Sets Sights on A24 Film After 'Moana 2'",
                source = "The Hollywood Reporter",
                timeAgo = "1d ago",
                summary = "Dwayne Johnson is in talks to star in an upcoming dramatic feature from acclaimed studio A24.",
                imageUrl =  R.drawable.dwayne_johnson
            ),
            NewsArticle(
                id = "4",
                headline = "'The Bear' Season 3: Everything We Know So Far",
                source = "IndieWire",
                timeAgo = "2d ago",
                summary = "FX's critically acclaimed series is set to return, and the kitchen is heating up more than ever.",
                imageUrl = R.drawable.the_bear_4
            )
        )
        val adapter = NewsArticleAdapter(newsArticles)
        recyclerView.adapter = adapter
    }

    private fun setupBornToday() {
        val bornTodayList = listOf(
            Celebrity("1","Tom Hanks", 69, R.drawable.tom_hank),
            Celebrity("2","Scarlett Johansson", 40, R.drawable.scarlett),
            Celebrity("3","Chris Evans", 43, R.drawable.chris_evans),
            Celebrity("4","Zendaya", 28, R.drawable.zendaya),
            Celebrity("5","Dwayne Johnson", 53, R.drawable.dwayne_johnson)
        )
        setupHorizontalRecyclerView(R.id.born_today_recyclerview, CelebrityAdapter(bornTodayList))
    }

    private fun setupMoreToExplore() {
        findViewById<ImageButton>(R.id.btn_social_facebook).setOnClickListener { openUrl("https://www.facebook.com/imdb") }
        findViewById<ImageButton>(R.id.btn_social_twitter).setOnClickListener { openUrl("https://www.twitter.com/imdb") }
        findViewById<ImageButton>(R.id.btn_social_instagram).setOnClickListener { openUrl("https://www.instagram.com/imdb") }
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
