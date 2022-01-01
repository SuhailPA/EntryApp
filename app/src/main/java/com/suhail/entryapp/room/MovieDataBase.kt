package com.suhail.entryapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suhail.entryapp.data.MovieResult
import com.suhail.entryapp.data.PageKey
import com.suhail.entryapp.data.Result

@Database(entities = [Result::class,PageKey::class], version = 1)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDao() : MovieDao

}