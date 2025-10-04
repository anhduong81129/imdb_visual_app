package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import usth.ict.group20.imdb.R

class HotSearchAdapter(private val searchTerms: List<String>) :
    RecyclerView.Adapter<HotSearchAdapter.SearchViewHolder>() {

    // This class holds the views for a single item
    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chip: Chip = view.findViewById(R.id.chip_text)
    }

    // This is called when a new view holder needs to be created
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_simple_chip, parent, false)
        return SearchViewHolder(view)
    }

    // This returns the total number of items in your list
    override fun getItemCount(): Int {
        return searchTerms.size
    }

    // This is called to display the data at a specific position
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchTerm = searchTerms[position]
        holder.chip.text = searchTerm

        // TODO: Set a click listener to perform a search when a chip is clicked
        holder.chip.setOnClickListener {
            // Handle the click event, e.g., show a Toast or start a search
        }
    }
}