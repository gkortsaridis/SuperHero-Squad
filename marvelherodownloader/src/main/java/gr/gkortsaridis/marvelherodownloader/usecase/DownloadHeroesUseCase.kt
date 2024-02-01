package gr.gkortsaridis.marvelherodownloader.usecase

import gr.gkortsaridis.marvelherodownloader.MarvelHeroesDownloader
import gr.gkortsaridis.marvelherodownloader.model.CharacterDataWrapper
import gr.gkortsaridis.marvelherodownloader.repository.MarvelHeroesRepository
import javax.inject.Inject

class DownloadHeroesUseCase @Inject constructor(
    private val repository: MarvelHeroesRepository
) {
    suspend operator fun invoke(): CharacterDataWrapper {
        if(MarvelHeroesDownloader.isSetup()) {
            val resp = repository.getHeroes(MarvelHeroesDownloader.heroesOffset)
            if(resp.isSuccessful) {
                val resp = resp.body() as CharacterDataWrapper
                MarvelHeroesDownloader.heroesOffset += resp.data.limit
                return resp
            } else {
                val error = resp.errorBody()
                throw RuntimeException(error?.string() ?: "Unknown Error")
            }
        } else {
            throw RuntimeException("MarvelApiDownloader not initialized")
        }

    }
}