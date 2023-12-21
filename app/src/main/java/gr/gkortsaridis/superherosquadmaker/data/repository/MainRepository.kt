package gr.gkortsaridis.superherosquadmaker.data.repository

import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import gr.gkortsaridis.superherosquadmaker.utils.DefaultDispatcherProvider
import gr.gkortsaridis.superherosquadmaker.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: MarvelApiHelper,
    private val roomDb: HeroesDatabase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) {
    suspend fun getHeroes() = apiHelper.getHeroes()

    suspend fun getSquad(): List<Hero> = withContext(dispatcherProvider.io()) {
        return@withContext roomDb.heroesDao().getSquad()
    }

    suspend fun addHeroToSquad(hero: Hero) = withContext(dispatcherProvider.io()) {
        roomDb.heroesDao().insertHero(hero)
    }

    suspend fun fireHeroFromSquad(hero: Hero) = withContext(dispatcherProvider.io()) {
        roomDb.heroesDao().deleteHero(hero)
    }
}