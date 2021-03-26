package com.coding.bookscan.entity.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Book (
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "book_isbn") val isbn : String,
    @ColumnInfo(name = "book_title") val title: String,
    @ColumnInfo(name = "book_author") val author : String,
    @ColumnInfo(name = "book_edition") val edition : String,
    @ColumnInfo(name = "book_summary") val summary : String,
    @ColumnInfo(name = "book_gender") val genre :String,
    @ColumnInfo(name = "book_release_date") val release_date:String,
    @ColumnInfo(name = "book_cover_name") val image:String,
    @ColumnInfo(name = "book_scan_date") var scanDate : String
) : Parcelable