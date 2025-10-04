package usth.ict.group20.imdb.models

data class Film(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: Double,
    val year: Int,
    val certificate: String? = null
)
