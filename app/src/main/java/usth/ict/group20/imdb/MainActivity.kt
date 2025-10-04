package usth.ict.group20.imdb

import usth.ict.group20.imdb.adapters.CelebrityAdapter
import usth.ict.group20.imdb.models.Celebrity
import usth.ict.group20.imdb.adapters.HotSearchAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.net.Uri
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupFeaturedCarousel()
        setupSearch()
        setupHotSearch()
        setupFeaturedToday()
        setupBornToday()
        setupWhatToWatchLists()
        setupExploreMoviesLists()
        setupMoreToExplore()
    }

    private fun setupFeaturedCarousel() {
        val viewPager: ViewPager2 = findViewById(R.id.featured_carousel_viewpager)
        // TODO: Create a CarouselAdapter and get real data
        // val items = listOf(...)
        // viewPager.adapter = CarouselAdapter(items)
    }

    private fun setupSearch() {
        val searchView: SearchView = findViewById(R.id.home_search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    // TODO: Navigate to a new SearchResultsActivity
                    Toast.makeText(this@MainActivity, "Searching for: $query", Toast.LENGTH_SHORT).show()
                    searchView.clearFocus() // Hide the keyboard
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // For implementing search suggestions as user types
                return false
            }
        })
    }

    private fun setupHorizontalRecyclerView(recyclerViewId: Int) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        // TODO: Create a generic adapter and pass data for each list
        // val items = listOf(...)
        // recyclerView.adapter = YourAdapter(items)
    }

    private fun setupVerticalRecyclerView(recyclerViewId: Int) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // TODO: Create a generic adapter and pass data
        // val items = listOf(...)
        // recyclerView.adapter = YourAdapter(items)
    }

    private fun setupHotSearch() {
        val recyclerView: RecyclerView = findViewById(R.id.hot_search_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Create some dummy data for now
        val hotSearches = listOf("The Marvels", "Oppenheimer", "Barbie", "Trending Actors")

        // Create an instance of your new adapter and attach it
        val adapter = HotSearchAdapter(hotSearches)
        recyclerView.adapter = adapter
    }

    private fun setupFeaturedToday() {
        setupVerticalRecyclerView(R.id.featured_today_recyclerview)
    }

    private fun setupBornToday() {
        val recyclerView: RecyclerView = findViewById(R.id.born_today_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Create dummy data using your new Celebrity data class
        val bornTodayList = listOf(
            Celebrity("Tom Hanks", "url_placeholder_1"),
            Celebrity("Scarlett Johansson", "url_placeholder_2"),
            Celebrity("Chris Evans", "url_placeholder_3"),
            Celebrity("Zendaya", "url_placeholder_4"),
            Celebrity("Dwayne Johnson", "url_placeholder_5")
        )

        // Create and attach your new CelebrityAdapter
        val adapter = CelebrityAdapter(bornTodayList)
        recyclerView.adapter = adapter
    }

    private fun setupWhatToWatchLists() {
        setupHorizontalRecyclerView(R.id.top_10_imdb_recyclerview)
        setupHorizontalRecyclerView(R.id.top_picks_recyclerview)
        setupHorizontalRecyclerView(R.id.in_theaters_recyclerview)
        setupHorizontalRecyclerView(R.id.fan_favorites_recyclerview)
        setupHorizontalRecyclerView(R.id.oscar_winners_recyclerview)
    }

    private fun setupExploreMoviesLists() {
        setupHorizontalRecyclerView(R.id.streaming_now_recyclerview)
        setupHorizontalRecyclerView(R.id.top_box_office_recyclerview)
        setupHorizontalRecyclerView(R.id.coming_soon_recyclerview)
    }

    private fun setupMoreToExplore() {
        // Popular Interests
        setupHorizontalRecyclerView(R.id.popular_interests_recyclerview)

        // Top News
        setupVerticalRecyclerView(R.id.top_news_more_recyclerview)

        // Social Media Links
        val facebookButton: ImageButton = findViewById(R.id.btn_social_facebook)
        facebookButton.setOnClickListener {
            // TODO: Replace with official IMDb Facebook URL
            openUrl("https://www.facebook.com/imdb")
        }

        val twitterButton: ImageButton = findViewById(R.id.btn_social_twitter)
        twitterButton.setOnClickListener {
            // TODO: Replace with official IMDb Twitter/X URL
            openUrl("https://www.twitter.com/imdb")
        }

        val instagramButton: ImageButton = findViewById(R.id.btn_social_instagram)
        instagramButton.setOnClickListener {
            // TODO: Replace with official IMDb Instagram URL
            openUrl("https://www.instagram.com/imdb")
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
