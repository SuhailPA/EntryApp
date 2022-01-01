package com.suhail.entryapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pageDetails")
data class PageKey(
    @PrimaryKey(autoGenerate = false)
    val title : String,
    val next : Int?,
    val prev : Int?
)
