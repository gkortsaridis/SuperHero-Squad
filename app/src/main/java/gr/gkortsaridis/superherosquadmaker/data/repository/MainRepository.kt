package gr.gkortsaridis.superherosquadmaker.data.repository

import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(
    private val apiHelper: MarvelApiHelper,
    private val roomDb: HeroesDatabase
) {

    suspend fun getHeroes() = apiHelper.getHeroes()

    suspend fun getSquad(): List<Hero> = withContext(Dispatchers.IO) {
        return@withContext roomDb.heroesDao().getSquad()
    }

    suspend fun addHeroToSquad(hero: Hero) = withContext(Dispatchers.IO) {
        roomDb.heroesDao().insertHero(hero)
    }

    suspend fun fireHeroFromSquad(hero: Hero) = withContext(Dispatchers.IO) {
        roomDb.heroesDao().deleteHero(hero)
    }
}