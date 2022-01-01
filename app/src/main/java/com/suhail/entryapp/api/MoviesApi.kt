package com.suhail.entryapp.api

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.suhail.entryapp.data.MovieResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {


    companion object{
            const val BASE_URL = "https://api.themoviedb.org/3/movie/"
    }

    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query ("api_key") api:String,
        @Query ("page") page :Int
    ): Response<MovieResult>

}