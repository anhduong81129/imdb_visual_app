package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import usth.ict.group20.imdb.R
import usth.ict.group20.imdb.models.Celebrity

// This adapter takes a list of Celebrity objects
class CelebrityAdapter(private val celebrities: List<Celebrity>) :
    RecyclerView.Adapter<CelebrityAdapter.CelebrityViewHolder>() {

    // The ViewHolder now holds references to both an ImageView and a TextView
    class CelebrityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.celebrity_image)
        val nameTextView: TextView = view.findViewById(R.id.celebrity_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CelebrityViewHolder {
        // Inflate the new item_celebrity_small.xml layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_celebrity_small, parent, false)
        return CelebrityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return celebrities.size
    }

    override fun onBindViewHolder(holder: CelebrityViewHolder, position: Int) {
        // Get the celebrity data for the current position
        val celebrity = celebrities[position]

        // Set the celebrity's name on the TextView
        holder.nameTextView.text = celebrity.name

        // TODO: Set the celebrity's image on the ImageView
        // For now, we'll just leave the placeholder. In a future step, you'll use
        // a library like Glide or Coil to load the image from a URL.
        // Example with Glide:
        // Glide.with(holder.itemView.context).load(celebrity.imageUrl).into(holder.imageView)

        holder.itemView.setOnClickListener {
            // TODO: Handle click to navigate to the celebrity's detail page
        }
    }
}