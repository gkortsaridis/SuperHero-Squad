package gr.gkortsaridis.marvelherodownloader.api

import gr.gkortsaridis.marvelherodownloader.MarvelHeroesDownloader
import gr.gkortsaridis.marvelherodownloader.model.CharacterDataWrapper
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class MarvelApiHelper @Inject constructor(
    private val apiService: MarvelApiService
) {
    suspend fun getHeroes(offset: Int): Response<CharacterDataWrapper> {
        val ts = calculateTimestamp()
        val hash = calculateHash(ts)
        return apiService.getHeroes(
            apiKey = MarvelHeroesDownloader.publicApiKey!!,
            timestamp = ts,
            hash = hash,
            limit = MarvelHeroesDownloader.characterLimit,
            offset = offset
        )
    }

    private fun calculateTimestamp(): String {
        return System.currentTimeMillis().toString()
    }
    private fun calculateHash(timestamp: String): String {
        return md5(timestamp + MarvelHeroesDownloader.privateApiKey + MarvelHeroesDownloader.publicApiKey)
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}