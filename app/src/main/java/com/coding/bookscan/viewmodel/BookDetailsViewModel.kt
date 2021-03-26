package com.coding.bookscan.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coding.bookscan.entity.AppDatabase
import com.coding.bookscan.entity.data.Book


sealed class BookDetailsViewModelState(open val errorMessage : String = "", open  val successMessage : String =""){
    object Loading : BookDetailsViewModelState()
    data class Success(val book: Book) : BookDetailsViewModelState()
    data class Failure(override val errorMessage: String) : BookDetailsViewModelState()
}
class BookDetailsViewModel : ViewModel() {
    private val bookDetailState = MutableLiveData<BookDetailsViewModelState>()

    fun getBookDetailState(): MutableLiveData<BookDetailsViewModelState> {
        return bookDetailState
    }

    fun loadBookDetail(book : Book){
        bookDetailState.postValue(BookDetailsViewModelState.Success(book))
    }

    fun deleteRow(db : AppDatabase,idRow:Int){
        db.bookDao().delete(idRow)
    }

}