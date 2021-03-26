package com.coding.bookscan.viewmodel

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.*
import androidx.room.Room
import com.coding.bookscan.entity.data.Book
import com.coding.bookscan.R
import com.coding.bookscan.entity.AppDatabase
import java.util.*
import java.util.Observer
import kotlin.concurrent.thread

sealed class BookListViewModelState(
    open val errorMessage: String = "",
    open val successMessage: String = ""
) {
    object Loading : BookListViewModelState()
    data class Success(val books: List<Book>, override val successMessage: String) :
        BookListViewModelState(successMessage = successMessage)

    data class Failure(override val errorMessage: String) :
        BookListViewModelState(errorMessage = errorMessage)
}

class BookListViewModel : ViewModel() {
    private val bookListState = MutableLiveData<BookListViewModelState>()

    fun getBookListState(): MutableLiveData<BookListViewModelState> {
        return bookListState
    }

    fun getBookList(db: AppDatabase, textSearched: String? = null) {
        //db.bookDao().getAllBook().observe(owner, androidx.lifecycle.Observer {
        var books: List<Book> = if (textSearched.isNullOrEmpty()) {
            db.bookDao().getAllBook()
        } else {
            db.bookDao().findBookByChar("%$textSearched%")
        }
        bookListState.postValue(BookListViewModelState.Loading)
        if (books.isNotEmpty()) {
            bookListState.postValue(
                BookListViewModelState.Success(
                    books,
                    "Liste de livres bien trouvée !"
                )
            )
        } else {
            bookListState.postValue(BookListViewModelState.Failure("Aucun Livre trouvé"))
        }

        /*thread { //A utiliser pour les inserts et delete quand .allowMainThreadQueries() n'est pas utilisé
            db.bookDao().insertBook(dataBook(0,1,"Le seigneur des anneaux","J.R.R Tolkien","Le Seigneur des Anneaux raconte la fin du Troisième Âge de la Terre du Milieu. Bilbo le Hobbit décide de quitter la Comté et laisse pour héritage à son neveu Frodo, Cul-de-Sac et l'Anneau qu'il avait trouvé lors de son aventure. Après de longue recherche, Gandalf apprend qu'il s'agit en fait de l'Anneau Unique, objet de pouvoir de Sauron, qui le cherche afin de conquérir la Terre du Milieu. Face à cette découverte, Gandalf demande à Frodo de prendre la route de Fondcombe. À partir de là, commencera le voyage de la Communauté de l'Anneau dont l'objectif désespéré sera sa destruction, dans la forge qui l'a vu naître au cœur du Mordor. Malheureusement, la Communauté sera séparée, les uns continuant la quête et les autres rejoignant les Royaumes du Rohan puis du Gondor qui participeront à la Guerre de l'Anneau. ","Fantaisy","1954",Date().toString(), R.drawable.lotr_cover_fr))
        }*/
    }
}