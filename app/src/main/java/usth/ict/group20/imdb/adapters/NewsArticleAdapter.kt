package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import usth.ict.group20.imdb.R
import usth.ict.group20.imdb.models.NewsArticle
import com.bumptech.glide.Glide


class NewsArticleAdapter(private val articles: List<NewsArticle>) :
    RecyclerView.Adapter<NewsArticleAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headlineTextView: TextView = itemView.findViewById(R.id.article_headline)
        val sourceTextView: TextView = itemView.findViewById(R.id.article_source) // Assuming you have these IDs
        val timeAgoTextView: TextView = itemView.findViewById(R.id.time_ago) // Assuming you have these IDs
        val imageView: ImageView = itemView.findViewById(R.id.article_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_article, parent, false) // Ensure this layout is correct
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.headlineTextView.text = article.headline
        holder.sourceTextView.text = article.source
        holder.timeAgoTextView.text = article.timeAgo

        // Use Glide to load the image from a URL
        Glide.with(holder.itemView.context)
            .load(article.imageUrl)
            .placeholder(R.drawable.loading_image) // Optional: show a placeholder while loading
            .error(R.drawable.error_image) // Optional: show an error image if loading fails
            .into(holder.imageView)
    }

    override fun getItemCount() = articles.size
}
    