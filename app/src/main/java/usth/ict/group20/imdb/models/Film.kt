package usth.ict.group20.imdb.models

data class Film(
    val id: Int,
    val title: String,
    val posterUrl: Int,
    val rating: Double,
    val year: Int,
    val certificate: String? = null,
    val category: String = ""
)
