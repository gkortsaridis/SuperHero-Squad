package gr.gkortsaridis.marvelherodownloader.repository

import gr.gkortsaridis.marvelherodownloader.api.MarvelApiHelper
import javax.inject.Inject

class MarvelHeroesRepository @Inject constructor(
    private val apiHelper: MarvelApiHelper,
) {
    suspend fun getHeroes(offset: Int) = apiHelper.getHeroes(offset)
}