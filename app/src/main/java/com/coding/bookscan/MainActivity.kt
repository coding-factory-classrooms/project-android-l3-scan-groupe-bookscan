package com.coding.bookscan

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import com.coding.bookscan.activity.ScannerActivity
import com.coding.bookscan.databinding.ActivityMainBinding
import com.coding.bookscan.viewmodel.MainViewModel
import com.coding.bookscan.viewmodel.MainViewModelState

class MainActivity : AppCompatActivity() {

    private val model : MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.getMainState().observe(this, Observer {
            state -> updateUi(state)
        })

        binding.goToScanButton.setOnClickListener {
            model.checkInternetConnection(this)
        }
    }

    private fun updateUi(state: MainViewModelState?) {
        when(state){
            is MainViewModelState.Success -> goToScan()
            is MainViewModelState.Failure -> Toast.makeText(this,state.errorMessage,Toast.LENGTH_SHORT).show()
            null -> TODO()
        }
    }

    private fun goToScan(){
        val intent = Intent(this,ScannerActivity::class.java)
        startActivity(intent)
    }
}