package usth.ict.group20.imdb.models

data class NewsArticle(
    val id: String,
    val headline: String,
    val source: String,         // e.g., "Variety"
    val timeAgo: String,        // e.g., "2h ago"
    val summary: String,
    val imageUrl: Int        // URL for the article image
)