package com.suhail.entryapp.di

import android.app.Application
import androidx.room.Room
import com.suhail.entryapp.api.MoviesApi
import com.suhail.entryapp.room.MovieDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleClass {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(MoviesApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideBackendAPI (retrofit: Retrofit) : MoviesApi = retrofit.create(MoviesApi::class.java)


    @Provides
    @Singleton
    fun provideDatabase(app:Application) : MovieDataBase =
        Room.databaseBuilder(app,MovieDataBase::class.java,"movie_db")
            .build()
}