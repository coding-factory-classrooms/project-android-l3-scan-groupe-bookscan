package com.coding.bookscan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.coding.bookscan.entity.data.Book
import com.coding.bookscan.R
import com.coding.bookscan.databinding.ActivityBookDetailsBinding
import com.coding.bookscan.viewmodel.BookDetailsViewModel
import com.coding.bookscan.viewmodel.BookDetailsViewModelState

class BookDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookDetailsBinding
    lateinit var book : Book
    val model : BookDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        book = intent.getParcelableExtra<Book>("Book")!!

        model.getBookDetailState().observe(this, Observer {
            book -> updateUi(book!!)
        })

        model.loadBookDetail(book)
    }

    private fun updateUi(state: BookDetailsViewModelState) {
        when(state){
            BookDetailsViewModelState.Loading -> TODO()
            is BookDetailsViewModelState.Success -> {
                binding.titleBookTextView.text = book.title
                binding.authorTextView.text = book.author
                binding.summaryTextView.text = book.summary
                binding.bookCoverImgView.setImageResource(book.coverId)
            }
            is BookDetailsViewModelState.Failure -> Toast.makeText(this,state.errorMessage,Toast.LENGTH_SHORT).show()
        }
    }
}