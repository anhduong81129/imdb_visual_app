package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import usth.ict.group20.imdb.R
import usth.ict.group20.imdb.models.Film

class FilmAdapter(private val films: List<Film>) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posterImageView: ImageView = view.findViewById(R.id.film_poster_image)
        val titleTextView: TextView = view.findViewById(R.id.film_title_text)
        // Add the following lines
        val yearTextView: TextView = view.findViewById(R.id.film_year_text)
        val ratingTextView: TextView = view.findViewById(R.id.film_rating_text)
        val certificateTextView: TextView = view.findViewById(R.id.film_certificate_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_film_poster, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        holder.titleTextView.text = film.title

        holder.yearTextView.text = film.year.toString()

        holder.ratingTextView.text = film.rating.toString()

        if (film.certificate != null) {
            holder.certificateTextView.text = film.certificate
            holder.certificateTextView.visibility = View.VISIBLE // Make it visible
        } else {
            holder.certificateTextView.visibility = View.GONE // Hide it if there is no certificate
        }

        holder.posterImageView.setImageResource(R.drawable.ic_launcher_background) // Placeholder
    }

    override fun getItemCount(): Int {
        return films.size
    }
}
