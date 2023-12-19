package gr.gkortsaridis.superherosquadmaker.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.model.ThumbnailConverter

@Database(entities = [Hero::class], version = 1)
@TypeConverters(ThumbnailConverter::class)
abstract class HeroesDatabase : RoomDatabase() {
    abstract fun heroesDao(): HeroDao
}