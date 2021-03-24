package com.coding.bookscan.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coding.bookscan.Book
import com.coding.bookscan.databinding.BookItemBinding

class BookAdapter(var books:List<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    class ViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookItemBinding.inflate(inflater,parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return books.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        with(holder.binding){
            bookTitleTextView.text = book.title
            authorTextView.text = book.title
            scanDateTextView.text = book.scanDate
            bookCoverImgView.setImageResource(book.coverId)
        }
    }

    fun updateDataSet(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }
}