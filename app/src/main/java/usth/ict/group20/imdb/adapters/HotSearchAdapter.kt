package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import usth.ict.group20.imdb.R

class HotSearchAdapter(
    private val hotSearches: List<String>,
    private val onItemClick: (String) -> Unit // <-- This is the required change
) : RecyclerView.Adapter<HotSearchAdapter.HotSearchViewHolder>() {

    class HotSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotSearchText: TextView = itemView.findViewById(R.id.hot_search_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hot_search, parent, false)
        return HotSearchViewHolder(view)
    }

    override fun getItemCount(): Int = hotSearches.size

    override fun onBindViewHolder(holder: HotSearchViewHolder, position: Int) {
        val searchTerm = hotSearches[position]
        holder.hotSearchText.text = searchTerm

        // 2. Set a click listener on the item view to trigger the lambda
        holder.itemView.setOnClickListener {
            onItemClick(searchTerm) // <-- This calls the code from MainActivity
        }
    }
}