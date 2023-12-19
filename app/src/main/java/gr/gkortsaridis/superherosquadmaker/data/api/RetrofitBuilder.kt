package gr.gkortsaridis.superherosquadmaker.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://gateway.marvel.com/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(ScalarsConverterFactory.create())

            .build() //Doesn't require the adapter
    }

    val apiService: MarvelApiService = getRetrofit().create(MarvelApiService::class.java)
}