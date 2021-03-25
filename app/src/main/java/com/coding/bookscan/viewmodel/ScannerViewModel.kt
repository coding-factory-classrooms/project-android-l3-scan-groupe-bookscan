package com.coding.bookscan.viewmodel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.coding.bookscan.databinding.ActivityScannerBinding
import com.coding.bookscan.entity.AppDatabase
import com.coding.bookscan.entity.data.Book
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread

const val CAMERA_REQUEST_CODE = 101
const val apiBaseUrl: String = "https://students.gryt.tech/bookscan/"

sealed class ScannerViewModelState(open val errorMessage : String = "", open val successMessage : String = "") {
    data class Success(val book : Book, override val successMessage: String) : ScannerViewModelState(successMessage = successMessage)
    data class Failure(override val errorMessage: String) : ScannerViewModelState(errorMessage = errorMessage)
}

class ScannerViewModel : ViewModel() {

    private val scannerState = MutableLiveData<ScannerViewModelState>()
    private val client = OkHttpClient()

    fun getScannerState(): MutableLiveData<ScannerViewModelState> {
        return scannerState
    }

    private fun makeRequest(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    fun setupPermissions(context: Context, activity: Activity){
        val permission:Int = ContextCompat.checkSelfPermission(context,android.Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED){
            Log.i("scanner","")
            makeRequest(activity)
        }
    }

    fun getBook(isbn : String, db : AppDatabase){
        var jsonReturn: String = ""
        val gson = Gson()
        val url = apiBaseUrl + isbn
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { jsonReturn = it.string() }
                Log.i("ApiUtils", jsonReturn)
                val book: Book = gson.fromJson(jsonReturn, Book::class.java)
                Log.i("ApiUtils", book.title)
                Log.i("ApiUtils", book.author)
                Log.i("ApiUtils", book.edition)
                Log.i("ApiUtils", book.genre)
                Log.i("ApiUtils", book.image)
                book.scanDate = Date().toString()
                db.bookDao().insertBook(book)
                scannerState.postValue(ScannerViewModelState.Success(book,"Livre trouvé $isbn"))
            }
        })
    }
}