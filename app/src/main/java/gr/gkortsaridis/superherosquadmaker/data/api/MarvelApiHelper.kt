package gr.gkortsaridis.superherosquadmaker.data.api

import gr.gkortsaridis.superherosquadmaker.data.model.CharacterDataWrapper
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class MarvelApiHelper @Inject constructor(
    private val apiService: MarvelApiService
) {
    suspend fun getHeroes(): Response<CharacterDataWrapper> {
        val ts = calculateTimestamp()
        val hash = calculateHash(ts)
        return apiService.getHeroes(
            apiKey = MARVEL_PUBLIC_KEY,
            timestamp = ts,
            hash = hash,
            limit = characterLimit,
            offset = characterOffset
        )
    }

    private fun calculateTimestamp(): String {
        return System.currentTimeMillis().toString()
    }
    private fun calculateHash(timestamp: String): String {
        return md5(timestamp + MARVEL_PRIVATE_KEY + MARVEL_PUBLIC_KEY)
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    companion object {
        const val MARVEL_PUBLIC_KEY = "301ea4d2491e82711f03ee01a782124c"
        const val MARVEL_PRIVATE_KEY = "622919e60ea63eb5355ac75a1ab7be2a65ea23e2"
        const val characterLimit = 100
        var characterOffset = 0
    }
}