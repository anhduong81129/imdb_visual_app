package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import usth.ict.group20.imdb.R
import usth.ict.group20.imdb.models.Celebrity
import com.bumptech.glide.Glide

// This adapter takes a list of Celebrity objects
class CelebrityAdapter(private val celebrities: List<Celebrity>) :
    RecyclerView.Adapter<CelebrityAdapter.CelebrityViewHolder>() {

    // The ViewHolder now holds references to both an ImageView and a TextView
    class CelebrityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.celebrity_name)
        val ageTextView: TextView = view.findViewById(R.id.celebrity_age)
        val imageView: ImageView = view.findViewById(R.id.celebrity_image)
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
        holder.ageTextView.text = "${celebrity.age}"
        holder.itemView.setOnClickListener {
            // TODO: Handle click to navigate to the celebrity's detail page
        }

        Glide.with(holder.itemView.context)
            .load(celebrity.imageUrl)
            .placeholder(R.drawable.loading_image)
            .error(R.drawable.error_image)
            .circleCrop()
            .into(holder.imageView)

    }
}