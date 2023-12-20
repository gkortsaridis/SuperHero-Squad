package gr.gkortsaridis.superherosquadmaker.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): MarvelApiService {
        return RetrofitBuilder.getRetrofit().create(MarvelApiService::class.java)
    }
}