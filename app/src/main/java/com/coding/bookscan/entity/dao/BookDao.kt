package com.coding.bookscan.entity.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.coding.bookscan.entity.data.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE book_title LIKE :bookTitle LIMIT 1")
    fun findByName(bookTitle: String): Book

    @Query("SELECT * FROM book WHERE book_isbn = :bookIsbn LIMIT 1")
    fun findById(bookIsbn: Int): Book

    @Insert
    fun insert(book: Book)

    @Delete
    fun delete(book: Book)
}