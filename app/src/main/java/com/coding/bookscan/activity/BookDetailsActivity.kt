package com.coding.bookscan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.coding.bookscan.Book
import com.coding.bookscan.R
import com.coding.bookscan.databinding.ActivityBookDetailsBinding

class BookDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookDetailsBinding
    lateinit var book : Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        book = intent.getParcelableExtra<Book>("Book")!!

        binding.titleBookTextView.text = book.title
        binding.authorTextView.text = book.author
        binding.summaryTextView.text = book.summary
        binding.bookCoverImgView.setImageResource(book.coverId)
    }
}