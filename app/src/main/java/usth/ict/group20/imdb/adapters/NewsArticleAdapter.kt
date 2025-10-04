package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import usth.ict.group20.imdb.R
import usth.ict.group20.imdb.models.NewsArticle

class NewsArticleAdapter(private val articles: List<NewsArticle>) :
    RecyclerView.Adapter<NewsArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val articleImage: ImageView = view.findViewById(R.id.article_image)
        val articleHeadline: TextView = view.findViewById(R.id.article_headline)
        val articleSource: TextView = view.findViewById(R.id.article_source)
        val articleSummary: TextView = view.findViewById(R.id.article_summary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        holder.articleHeadline.text = article.headline
        holder.articleSummary.text = article.summary
        // We can combine source and time for the source TextView
        holder.articleSource.text = "${article.source} • ${article.timeAgo}"

        // TODO: Use a library like Glide or Coil to load the image from article.imageUrl
        // Glide.with(holder.itemView.context).load(article.imageUrl).into(holder.articleImage)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}
    