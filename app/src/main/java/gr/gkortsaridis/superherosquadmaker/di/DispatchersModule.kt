package gr.gkortsaridis.superherosquadmaker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.gkortsaridis.superherosquadmaker.utils.DefaultDispatcherProvider
import gr.gkortsaridis.superherosquadmaker.utils.DispatcherProvider
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DispatchersModule()  {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}