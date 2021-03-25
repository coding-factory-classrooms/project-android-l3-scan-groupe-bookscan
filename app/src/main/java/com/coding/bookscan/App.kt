package com.coding.bookscan

import android.app.Application
import androidx.room.Room
import com.coding.bookscan.entity.AppDatabase

class App : Application() {
    companion object {
        lateinit var db : AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "BookScan"
        ).allowMainThreadQueries().build()
    }
}