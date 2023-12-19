package gr.gkortsaridis.superherosquadmaker.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize

/**
 * Decided to slim down the data classes to the basic data i will need for this demo
 */
data class CharacterDataWrapper(
    val code: Int,
    val status: String,
    val data: CharacterDataContainer,
)

data class CharacterDataContainer(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Hero>
)

@Parcelize
@Entity
data class Hero(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "hero_name") val name: String,

    @TypeConverters(ThumbnailConverter::class)
    @ColumnInfo(name = "hero_thumbnail") val thumbnail: Thumbnail,
    @ColumnInfo(name = "hero_description") val description: String,
) : Parcelable

@Parcelize
@Entity
data class Thumbnail(
    @PrimaryKey val path: String,
    @ColumnInfo(name = "thumbnail_extension") val extension: String,
): Parcelable

class ThumbnailConverter {
    @TypeConverter
    fun toThumbnail(value: String): Thumbnail =
        value.split("_#_").let {
            Thumbnail(
                it.first(),
                it.last()
            )
        }

    @TypeConverter
    fun toString(thumbnail: Thumbnail): String = "${thumbnail.path}_#_${thumbnail.extension}"

}