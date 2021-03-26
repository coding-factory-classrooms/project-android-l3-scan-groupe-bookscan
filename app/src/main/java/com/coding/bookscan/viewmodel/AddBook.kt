package com.coding.bookscan.viewmodel

import androidx.lifecycle.MutableLiveData
import com.coding.bookscan.entity.AppDatabase
import com.coding.bookscan.entity.data.Book
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.util.*

sealed class AddBookState(open val errorMessage : String = "", open val successMessage : String = "") {
    data class Success(val book : Book, override val successMessage: String) : AddBookState(successMessage = successMessage)
    data class Failure(override val errorMessage: String) : AddBookState(errorMessage = errorMessage)
}

class AddBook{

    companion object{

        fun getBook(isbn : String, db : AppDatabase) {
            val client = OkHttpClient()
            val bookState = MutableLiveData<AddBookState>()

            var jsonReturn: String = ""
            val gson = Gson()
            lateinit var book: Book
            if (isbn.isNullOrEmpty() || isbn.length != 13) {
                bookState.postValue(
                    AddBookState.Failure("Format de code barre incorrect")
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
                        bookState.postValue(AddBookState.Failure("Erreur de requête"))
                        return
                    }
                    override fun onResponse(call: Call, response: Response) {
                        response.body()?.let { jsonReturn = it.string() }
                        if (jsonReturn.isNullOrEmpty()) {
                            bookState.postValue(
                                AddBookState.Failure("Aucune réponse trouvée")
                            )
                            return
                        }
                        try {
                            book = gson.fromJson(jsonReturn, Book::class.java)
                        } catch (e: Exception) {
                            bookState.postValue(
                                AddBookState.Failure("Erreur de donnée récupérée")
                            )
                            return
                        }
                        book.scanDate = Date().toString()
                        try {
                            db.bookDao().insertBook(book)
                        } catch (e: Exception) {
                            bookState.postValue(
                                AddBookState.Failure("Erreur lors de l'ajout en base")
                            )
                            return
                        }
                        bookState.postValue(
                            AddBookState.Success(
                                book,
                                "Livre trouvé $isbn"
                            )
                        )
                    }
                })
            } catch (e: Exception) {
                bookState.postValue(AddBookState.Failure("Erreur de récupération de donnée"))
                return
            }
        }


    }



}

