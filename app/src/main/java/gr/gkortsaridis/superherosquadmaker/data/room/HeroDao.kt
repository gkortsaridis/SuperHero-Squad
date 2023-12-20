package gr.gkortsaridis.superherosquadmaker.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import gr.gkortsaridis.superherosquadmaker.data.model.Hero

@Dao
interface HeroDao {
    @Query("SELECT * FROM hero")
    fun getSquad(): List<Hero>

    @Query("SELECT * FROM hero WHERE id IN (:heroId) LIMIT 1")
    fun loadAById(heroId: Int): Hero

    @Insert
    fun insertHero(hero: Hero)

    @Delete
    fun deleteHero(hero: Hero)
}
