package com.alons.marvel_universe.di

import com.alons.marvel_universe.data.data_source.MarvelApi
import com.alons.marvel_universe.data.repository.MarvelRepositoryImpl
import com.alons.marvel_universe.domain.repository.MarvelRepository
import com.alons.marvel_universe.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMarvelApi():MarvelApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelApi::class.java)
    }
    @Provides
    @Singleton
    fun provideMarvelRepository(api:MarvelApi):MarvelRepository{
        return MarvelRepositoryImpl(api)
    }
}