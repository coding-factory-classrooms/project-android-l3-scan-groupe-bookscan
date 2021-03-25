package com.coding.bookscan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.coding.bookscan.entity.data.Book
import com.google.gson.Gson
import kotlinx.coroutines.awaitAll
import okhttp3.*
import java.io.IOException

private val apiBaseUrl: String = "https://students.gryt.tech/bookscan/"
private val bookId: String = "9782253169789"

class ApiUtils : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("ApiUtils", "OnCreate !")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getBooks(apiBaseUrl + bookId)
    }

    fun getBooks(url: String): String {
        var jsonReturn: String = ""
        val gson = Gson()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { jsonReturn = it.string() }
                Log.i("ApiUtils", jsonReturn)
                var book: Book = gson.fromJson(jsonReturn, Book::class.java)
                Log.i("ApiUtils", book.title)
                Log.i("ApiUtils", book.author)
                Log.i("ApiUtils", book.edition)
                Log.i("ApiUtils", book.genre)
                Log.i("ApiUtils", book.image)
            }
        })
        return jsonReturn
    }
}