package gr.gkortsaridis.superherosquadmaker.domain.repository.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import gr.gkortsaridis.marvelherodownloader.model.Hero

@Dao
interface HeroDao {
    @Query("SELECT * FROM hero")
    suspend fun getSquad(): List<Hero>

    @Query("SELECT * FROM hero WHERE id IN (:heroId) LIMIT 1")
    suspend fun loadAById(heroId: Int): Hero?

    @Insert
    suspend fun insertHero(hero: Hero)

    @Delete
    suspend fun deleteHero(hero: Hero)
}
