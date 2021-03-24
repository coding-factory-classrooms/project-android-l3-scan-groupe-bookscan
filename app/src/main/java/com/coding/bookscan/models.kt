package com.coding.bookscan

data class Book(
    val title: String,
    val author : String,
    val summary : String,
    //val genre :Genre,
    val releaseDate:String,
    val scanDate : String,
    val coverId:Int
)