package com.coding.bookscan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { jsonReturn = it.string() }
                Log.i("ApiUtils", jsonReturn)
            }
        })
        return jsonReturn
    }
}