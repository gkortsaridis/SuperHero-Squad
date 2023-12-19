package gr.gkortsaridis.superherosquadmaker.data.api

import java.math.BigInteger
import java.security.MessageDigest

class MarvelApiHelper(private val apiService: MarvelApiService) {
    suspend fun getHeroes() = apiService.getHeroes(
        apiKey = MARVEL_PUBLIC_KEY,
        timestamp = calculateTimestamp(),
        hash = calculateHash())


    private fun calculateTimestamp(): String {
        return System.currentTimeMillis().toString()
    }
    private fun calculateHash(): String {
        return md5(calculateTimestamp() + MARVEL_PRIVATE_KEY + MARVEL_PUBLIC_KEY)
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    companion object {
        const val MARVEL_PUBLIC_KEY = "301ea4d2491e82711f03ee01a782124c"
        const val MARVEL_PRIVATE_KEY = "622919e60ea63eb5355ac75a1ab7be2a65ea23e2"
    }
}