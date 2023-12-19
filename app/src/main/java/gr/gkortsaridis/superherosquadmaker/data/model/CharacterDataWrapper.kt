package gr.gkortsaridis.superherosquadmaker.data.model

data class CharacterDataWrapper(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: CharacterDataContainer,
)

data class CharacterDataContainer(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Hero>
)

data class Hero(
    val id: Int,
    val name: String,
    val description: String,
    val modified: String,
    val thumbnail: Thumbnail,
    val resourceURI: String,
    val comics: Comics,
    val series: Series,
    val stories: Stories,
    //private val events: Events,
    //private val urls: List<Url>
)

data class Thumbnail(
    private val path: String,
    private val extension: String
)

data class Comics(
    private val available: Int,
    private val collectionURI: String,
    //private val items: List<Item>,
    private val returned: Int
)

data class Series(
    private val available: Int,
    private val collectionURI: String,
    //private val items: List<Item>,
    private val returned: Int
)

data class Stories(
    private val available: Int,
    private val collectionURI: String,
)