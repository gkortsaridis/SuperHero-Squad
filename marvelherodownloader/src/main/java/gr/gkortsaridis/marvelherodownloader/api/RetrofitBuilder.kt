package gr.gkortsaridis.marvelherodownloader.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://gateway.marvel.com/"

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

            .build() //Doesn't require the adapter
    }
}