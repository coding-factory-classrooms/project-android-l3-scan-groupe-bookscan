package com.coding.bookscan.activity

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.coding.bookscan.entity.data.Book
import com.coding.bookscan.databinding.BookItemBinding
import com.coding.bookscan.formatedDate
import com.coding.bookscan.formatedImgName

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        with(holder.binding){
            bookTitleTextView.text = book.title
            authorTextView.text = book.author
            scanDateTextView.text = book.formatedDate()
            with(holder.itemView.context){
                bookCoverImgView.setImageResource(this.resources.getIdentifier(book.formatedImgName(),"drawable",this.packageName))
            }

            constraintLayout.setOnClickListener{
                with(holder.itemView.context){
                    var intent = Intent(this, BookDetailsActivity::class.java)
                    intent.putExtra("Book", book)
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    fun updateDataSet(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }
}