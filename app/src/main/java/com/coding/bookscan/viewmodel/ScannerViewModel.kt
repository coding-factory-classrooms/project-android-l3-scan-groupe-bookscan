package com.coding.bookscan.viewmodel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
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
import com.coding.bookscan.formatedImgName
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread

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

    fun getBook(isbn : String, db : AppDatabase) {
        var jsonReturn: String = ""
        val gson = Gson()
        lateinit var book: Book
        if (isbn.isNullOrEmpty() || isbn.length != 13) {
            scannerState.postValue(
                ScannerViewModelState.Failure("Format de code barre incorrect")
            )
            return
        }
        val url = apiBaseUrl + isbn
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    scannerState.postValue(ScannerViewModelState.Failure("Erreur de requête"))
                    return
                }
                override fun onResponse(call: Call, response: Response) {
                    response.body()?.let { jsonReturn = it.string() }
                    if (jsonReturn.isNullOrEmpty()) {
                        scannerState.postValue(
                            ScannerViewModelState.Failure("Aucune réponse trouvée")
                        )
                        return
                    }
                    try {
                        book = gson.fromJson(jsonReturn, Book::class.java)
                    } catch (e: Exception) {
                        scannerState.postValue(
                            ScannerViewModelState.Failure("Erreur de donnée récupérée")
                        )
                        return
                    }
                    book.scanDate = Date().toString()
                    try {
                        db.bookDao().insertBook(book)
                    } catch (e: Exception) {
                        scannerState.postValue(
                            ScannerViewModelState.Failure("Erreur lors de l'ajout en base")
                        )
                        return
                    }
                    scannerState.postValue(
                        ScannerViewModelState.Success(
                            book,
                            "Livre trouvé $isbn"
                        )
                    )
                }
            })
        } catch (e: Exception) {
            scannerState.postValue(ScannerViewModelState.Failure("Erreur de récupération de donnée"))
            return
        }
        scannerState.postValue(ScannerViewModelState.Failure("Coucou"))
    }
}