package com.coding.bookscan.entity

import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.coding.bookscan.entity.dao.BookDao
import com.coding.bookscan.entity.data.Book

@Database(entities = [Book::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao() : BookDao
}