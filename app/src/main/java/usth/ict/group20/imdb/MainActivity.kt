package usth.ict.group20.imdb

import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.database.MatrixCursor
import android.provider.BaseColumns
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import usth.ict.group20.imdb.adapters.CarouselAdapter
import usth.ict.group20.imdb.adapters.CarouselItem
import usth.ict.group20.imdb.adapters.CelebrityAdapter
import usth.ict.group20.imdb.models.Celebrity
import usth.ict.group20.imdb.adapters.FilmAdapter
import usth.ict.group20.imdb.models.Film
import usth.ict.group20.imdb.adapters.HotSearchAdapter
import usth.ict.group20.imdb.adapters.NewsArticleAdapter
import usth.ict.group20.imdb.models.NewsArticle

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val scrollHandler = Handler(Looper.getMainLooper())
    private lateinit var scrollRunnable: Runnable

    private val allFilms = mutableListOf<Film>()
    private lateinit var searchView: SearchView
    private lateinit var suggestionAdapter: SimpleCursorAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // Often causes layout issues with complex layouts, can be disabled.
        setContentView(R.layout.activity_main)

        // Setup all UI components
        setupAllFilmData()
        setupSearchSuggestions()
        setupSearch()
        setupFeaturedCarousel()
        setupHotSearch()
        setupFeaturedToday() // This was previously empty
        setupWhatToWatchLists()
        setupBornToday()
        setupExploreMoviesLists()
        setupMoreToExplore()
    }

    private fun setupFeaturedCarousel() {
        // The viewPager variable is now at the class level
        viewPager = findViewById(R.id.featured_carousel_viewpager)
        // Create dummy data. Replace with your actual drawable resources.
        val items = listOf(
            CarouselItem(R.drawable.oppenheimer),
            CarouselItem(R.drawable.dune2),
            CarouselItem(R.drawable.interstellar),
            CarouselItem(R.drawable.kungfu4),
            CarouselItem(R.drawable.back_to_the_future),
            CarouselItem(R.drawable.the_godfather),
            CarouselItem(R.drawable.the_matrix),
            CarouselItem(R.drawable.parasite),
            CarouselItem(R.drawable.shawshank),
            CarouselItem(R.drawable.the_dark_knight)
        )
        viewPager.adapter = CarouselAdapter(items)

        // This is the code that makes the carousel auto-scroll
        scrollRunnable = Runnable {
            var currentItem = viewPager.currentItem
            // If it's the last item, go back to the first, otherwise go to the next
            currentItem = if (currentItem == items.size - 1) 0 else currentItem + 1
            viewPager.setCurrentItem(currentItem, true) // Use true for smooth scroll
        }

        // Start the auto-scrolling
        startAutoScroll()
    }

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

    private fun startAutoScroll() {
        // Schedule the runnable to run every 3 seconds (3000 milliseconds)
        scrollHandler.postDelayed(scrollRunnable, 3000)

        // Also, reset the timer whenever the user manually swipes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollHandler.removeCallbacks(scrollRunnable) // Remove the old callback
                scrollHandler.postDelayed(scrollRunnable, 3000) // Schedule a new one
            }
        })
    }

    override fun onPause() {
        super.onPause()
        // Stop auto-scrolling when the app is not visible to save resources
        scrollHandler.removeCallbacks(scrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        // Resume auto-scrolling when the app is visible again
        scrollHandler.postDelayed(scrollRunnable, 3000)
    }

    private fun setupSearch() {
        searchView = findViewById(R.id.home_search_view)
        searchView.suggestionsAdapter = suggestionAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    // Launch the new activity
                    val intent = Intent(this@MainActivity, SearchResultsActivity::class.java).apply {
                        putExtra("SEARCH_QUERY", query)
                    }
                    startActivity(intent)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, "filmTitle"))
                newText?.let { query ->
                    // Only show suggestions if the user types something
                    if (query.isNotBlank()) {
                        // Filter the master list of films
                        allFilms.forEachIndexed { index, film ->
                            if (film.title.contains(query, ignoreCase = true)) {
                                // Add matching film titles to the cursor
                                cursor.addRow(arrayOf(index, film.title))
                            }
                        }
                    }
                }
                // Update the adapter with the new cursor containing suggestions
                suggestionAdapter.changeCursor(cursor)
                return true
            }
        })
        // Handle clicks on a suggestion
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                // We don't need to do anything special when an item is just highlighted.
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = suggestionAdapter.cursor
                // Move to the clicked item in the cursor
                if (cursor.moveToPosition(position)) {
                    // Get the suggestion text from the "filmTitle" column
                    val suggestion = cursor.getString(cursor.getColumnIndexOrThrow("filmTitle"))
                    // Set the suggestion as the query text and immediately submit the search
                    searchView.setQuery(suggestion, true)
                }
                return true
            }
        })
    }

    private fun setupSearchSuggestions() {
        val from = arrayOf("filmTitle")
        val to = intArrayOf(android.R.id.text1)
        suggestionAdapter = SimpleCursorAdapter(
            this,
            // v-- THE FIX IS HERE --v
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            // ^-- THE FIX IS HERE --^
            null, // The cursor is initially null
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
        val hotSearches = listOf("The Marvels", "Oppenheimer", "Barbie", "Trending Actors")
        setupHorizontalRecyclerView(R.id.hot_search_recyclerview, HotSearchAdapter(hotSearches))
    }

    private fun setupFeaturedToday() {
        // This section will now be populated with news articles.
        val featuredNews = listOf(
            NewsArticle(10, "Box Office Buzz: 'New Movie' Dominates", "BoxOfficeMojo", "1h ago", "The latest blockbuster has smashed opening weekend records.", R.drawable.the_creator),
            NewsArticle(11, "Critically Acclaimed 'Indie Gem' Gets Wide Release", "IndieWire", "3h ago", "After a successful festival run, the film is now available nationwide.", R.drawable.avatar_fire_and_ash)
        )
        // Reusing the NewsArticleAdapter for a vertical list
        setupVerticalRecyclerView(R.id.featured_today_recyclerview, NewsArticleAdapter(featuredNews))
    }

    private fun setupWhatToWatchLists() {
        val top10List = listOf(
            Film(1, "The Shawshank Redemption", R.drawable.shawshank, 9.3, 1994, "R"),
            Film(2, "The Godfather", R.drawable.the_godfather, 9.2, 1972, "R"),
            Film(3, "The Dark Knight", R.drawable.the_dark_knight, 9.0, 2008, "PG-13"),
            Film(4, "Pulp Fiction", R.drawable.pulp, 8.9, 1994, "R")
        )

        setupHorizontalRecyclerView(R.id.top_10_recyclerview, FilmAdapter(top10List) )

        val topPicksList = listOf(
            Film(10, "Interstellar", R.drawable.interstellar, 8.7, 2014, "PG-13"),
            Film(11, "The Matrix", R.drawable.the_matrix, 8.7, 1999, "R"),
            Film(12, "Goodfellas", R.drawable.goodfellas, 8.7, 1990, "R")
        )
        setupHorizontalRecyclerView(R.id.top_picks_recyclerview, FilmAdapter(topPicksList) )

        val inTheatersList = listOf(
            Film(20, "Dune: Part Two", R.drawable.dune2, 8.8, 2024, "PG-13"),
            Film(21, "Kung Fu Panda 4", R.drawable.kungfu4, 6.8, 2024, "PG")
        )
        setupHorizontalRecyclerView(R.id.in_theaters_recyclerview, FilmAdapter(inTheatersList) )

        val fanFavoritesList = listOf(
            Film(30, "Back to the Future", R.drawable.back_to_the_future, 8.5, 1985, "PG"),
            Film(31, "Jurassic Park", R.drawable.jurassic_park, 8.2, 1993, "PG-13")
        )
        setupHorizontalRecyclerView(R.id.fans_favorites_recyclerview, FilmAdapter(fanFavoritesList))

        val oscarWinnersList = listOf(
            Film(40, "Oppenheimer", R.drawable.oppenheimer, 8.6, 2023, "R"),
            Film(41, "Parasite", R.drawable.parasite, 8.5, 2019, "R")
        )
        setupHorizontalRecyclerView(R.id.oscar_winners_recyclerview, FilmAdapter(oscarWinnersList))
    }


    private fun setupBornToday() {
        val bornTodayList = listOf(
            Celebrity("Tom Hanks", 69, "url_placeholder_1"),
            Celebrity("Scarlett Johansson", 40, "url_placeholder_2"),
            Celebrity("Chris Evans", 43, "url_placeholder_3"),
            Celebrity("Zendaya", 28, "url_placeholder_4"),
            Celebrity("Dwayne Johnson", 53, "url_placeholder_5")
        )
        setupHorizontalRecyclerView(R.id.born_today_recyclerview, CelebrityAdapter(bornTodayList))
    }

    private fun setupExploreMoviesLists() {
        val streamingNowList = listOf(
            Film(50, "Glass Onion", R.drawable.glass_onion, 7.1, 2022, "PG-13")
        )
        setupHorizontalRecyclerView(R.id.streaming_now_recyclerview, FilmAdapter(streamingNowList))

        val topBoxOfficeList = listOf(
            Film(60, "Avatar: The Way of Water", R.drawable.avatar, 7.6, 2022, "PG-13")
        )
        setupHorizontalRecyclerView(R.id.top_box_office_recyclerview, FilmAdapter(topBoxOfficeList))

        val comingSoonList = emptyList<Film>()
        setupHorizontalRecyclerView(R.id.coming_soon_recyclerview, FilmAdapter(comingSoonList))
    }

    private fun setupMoreToExplore() {
        /*val popularInterestsList = emptyList<Film>()
        setupHorizontalRecyclerView(R.id.popular_interests_recyclerview, FilmAdapter(popularInterestsList))

        val newsList = listOf(
            NewsArticle(1, "Major Movie News Headline", "Variety", "2h ago", "This is a short summary...", "url_placeholder"),
            NewsArticle(2, "Another Big Casting Announcement", "Deadline", "5h ago", "Details about the upcoming blockbuster...", "url_placeholder"),
            NewsArticle(3, "Festival Film Sells for Record Price", "THR", "1d ago", "The indie darling is heading to a major studio...", "url_placeholder")
        )
        setupVerticalRecyclerView(R.id.top_news_more_recyclerview, NewsArticleAdapter(newsList))

         */
        findViewById<ImageButton>(R.id.btn_social_facebook).setOnClickListener { openUrl("https://www.facebook.com/imdb") }
        findViewById<ImageButton>(R.id.btn_social_twitter).setOnClickListener { openUrl("https://www.twitter.com/imdb") }
        findViewById<ImageButton>(R.id.btn_social_instagram).setOnClickListener { openUrl("https://www.instagram.com/imdb") }

    }


    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
