package com.suhail.entryapp.repository

import com.suhail.entryapp.api.MoviesApi
import com.suhail.entryapp.data.PageKey
import com.suhail.entryapp.data.Result
import com.suhail.entryapp.room.MovieDataBase
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api : MoviesApi,
    private val database : MovieDataBase
) {

     fun getAllMoviesDao() = database.movieDao().getAllMovies()

    suspend fun getAllPageKeys(title:String) = database.movieDao().getAllPageKeys(title)

    suspend fun getNowPlayingMovies(apiKey:String,page:Int) = api.getNowPlayingMovies(apiKey,page)

    suspend fun deleteAllPageKeys()= database.movieDao().deleteAllPageDetails()

    suspend fun deleteAllMovies () = database.movieDao().deleteAllMovies()

    suspend fun insertAllKeys(pages : List<PageKey>) = database.movieDao().insertAllKeys(pages)

    suspend fun insertMovies(movies : List<Result>) = database.movieDao().addAllMovies(movies)

}