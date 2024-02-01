package gr.gkortsaridis.superherosquadmaker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gr.gkortsaridis.superherosquadmaker.domain.repository.room.HeroDao
import gr.gkortsaridis.superherosquadmaker.domain.repository.room.HeroesDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideHeroesDao(appDatabase: HeroesDatabase): HeroDao {
        return appDatabase.heroesDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): HeroesDatabase {
        return Room.databaseBuilder(
            appContext,
            HeroesDatabase::class.java,
            "heroes-db"
        ).build()
    }
}