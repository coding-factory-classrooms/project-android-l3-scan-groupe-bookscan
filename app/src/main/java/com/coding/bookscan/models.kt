package com.coding.bookscan

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    val title: String,
    val author : String,
    val summary : String,
    //val genre :Genre,
    val releaseDate:String,
    val scanDate : String,
    val coverId:Int
) : Parcelable