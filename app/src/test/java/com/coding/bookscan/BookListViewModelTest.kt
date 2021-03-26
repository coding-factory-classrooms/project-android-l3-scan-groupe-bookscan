package com.coding.bookscan

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.coding.bookscan.entity.AppDatabase
import com.coding.bookscan.entity.dao.BookDao
import com.coding.bookscan.entity.data.Book
import com.coding.bookscan.viewmodel.BookListViewModel
import com.coding.bookscan.viewmodel.BookListViewModelState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.robin.movie.testObserver

class BookListViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `get book list yields Success`() {
        val model = BookListViewModel()
        val observer = model.getBookListState().testObserver()
        val book = Book(1, "1234", "Oui", "","","","","","","",1)
        val db = mock<AppDatabase>()
        val bookDao = mock<BookDao>()
        whenever(bookDao.getAllBook()).thenReturn(listOf(book))
        whenever(db.bookDao()).thenReturn(bookDao)

        model.getBookList(db)
        Assert.assertEquals(
            listOf(
                BookListViewModelState.Loading,
                BookListViewModelState.Success(listOf(book) ,"Liste de livres bien trouvée !")
            ),
            observer.observedValues
        )
    }

    @Test
    fun `get book list yields Failure`() {
        val model = BookListViewModel()
        val observer = model.getBookListState().testObserver()
        val db = mock<AppDatabase>()
        val bookDao = mock<BookDao>()
        whenever(bookDao.getAllBook()).thenReturn(listOf())
        whenever(db.bookDao()).thenReturn(bookDao)

        model.getBookList(db)
        Assert.assertEquals(
            listOf(
                BookListViewModelState.Loading,
                BookListViewModelState.Failure("Aucun Livre trouvé")
            ),
            observer.observedValues
        )
    }
}