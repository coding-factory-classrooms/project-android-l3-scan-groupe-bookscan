package com.coding.bookscan.entity.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.coding.bookscan.entity.data.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBook(): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE book_title LIKE :bookTitle LIMIT 1")
    fun findBookByName(bookTitle: String): LiveData<Book>

    @Query("SELECT * FROM book WHERE book_isbn = :bookIsbn LIMIT 1")
    fun findBookById(bookIsbn: Int): Book

    @Insert
    fun insertBook(book: Book)

    @Delete
    fun deleteBook(book: Book)
}