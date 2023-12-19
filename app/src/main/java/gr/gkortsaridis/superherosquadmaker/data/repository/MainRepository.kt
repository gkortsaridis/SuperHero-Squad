package gr.gkortsaridis.superherosquadmaker.data.repository

import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase

class MainRepository(
    private val apiHelper: MarvelApiHelper,
    private val roomDb: HeroesDatabase
) {

    suspend fun getHeroes() = apiHelper.getHeroes()

    suspend fun getSquad() = roomDb.heroesDao().getSquad()
}