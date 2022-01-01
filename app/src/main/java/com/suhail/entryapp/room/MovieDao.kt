package com.suhail.entryapp.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.suhail.entryapp.data.PageKey
import com.suhail.entryapp.data.Result

@Dao
interface MovieDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMovies(movieList:List<Result>)

    @Query("SELECT * FROM movieDetails")
    fun getAllMovies():PagingSource<Int,Result>

    @Query("DELETE FROM movieDetails")
    suspend fun deleteAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(pages:List<PageKey>)

    @Query("SELECT * FROM pageDetails WHERE title = :title")
    suspend fun getAllPageKeys(title:String):PageKey

    @Query("DELETE FROM pageDetails")
    suspend fun deleteAllPageDetails()
}