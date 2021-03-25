package com.coding.bookscan.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.coding.bookscan.App
import com.coding.bookscan.R
import com.coding.bookscan.databinding.ActivityBookListBinding
import com.coding.bookscan.viewmodel.BookListViewModel
import com.coding.bookscan.viewmodel.BookListViewModelState

class BookListActivity : AppCompatActivity() {
    private val model : BookListViewModel by viewModels()

    private lateinit var binding: ActivityBookListBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.getBookListState().observe(this, Observer {
            books -> updateUi(books!!)
        })

        adapter = BookAdapter(listOf())
        binding.bookListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookListRecyclerView.adapter = adapter

        model.getBookList(App.db,this)
    }

    private fun updateUi(state: BookListViewModelState) {
        when(state){
            BookListViewModelState.Loading -> Toast.makeText(this@BookListActivity,"Chargement", Toast.LENGTH_SHORT).show()
            is BookListViewModelState.Success -> adapter.updateDataSet(state.books)
            is BookListViewModelState.Failure -> Toast.makeText(this@BookListActivity,state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}