package gr.gkortsaridis.superherosquadmaker.data.repository

import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper

class MainRepository(
    private val apiHelper: MarvelApiHelper
) {

    suspend fun getHeroes() = apiHelper.getHeroes()
}