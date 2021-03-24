package com.coding.bookscan.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book (
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "book_isbn") val isbn : Int,
    @ColumnInfo(name = "book_title") val title: String,
    @ColumnInfo(name = "book_author") val author : String,
    @ColumnInfo(name = "book_summary") val summary : String,
    @ColumnInfo(name = "book_gender") val genre :String,
    @ColumnInfo(name = "book_release_date") val releaseDate:String,
    @ColumnInfo(name = "book_scan_date") val scanDate : String,
    @ColumnInfo(name = "book_cover_id") val coverId:Int
)