package com.coding.bookscan.activity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.budiyev.android.codescanner.CodeScanner
import com.coding.bookscan.databinding.ActivityScannerBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.coding.bookscan.entity.data.Book
import com.coding.bookscan.viewmodel.CAMERA_REQUEST_CODE
import com.coding.bookscan.viewmodel.ScannerViewModel
import com.coding.bookscan.viewmodel.ScannerViewModelState


class ScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityScannerBinding
    private val model : ScannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.setupPermissions(this,this)

        model.getScannerState().observe(this, Observer {
            state -> uiResponse(state)
        })

        codeScanner(binding)
    }

    private fun uiResponse(state: ScannerViewModelState?) {
        when(state){
            is ScannerViewModelState.Success -> goToDetails(state.book)
            is ScannerViewModelState.Failure -> TODO()
            null -> TODO()
        }
    }

    private fun goToDetails(book: Book) {
        Log.i("scanner", "goToDetails: $book")
        var intent = Intent(this,BookDetailsActivity::class.java)
        intent.putExtra("Book",book)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty()|| grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Log.i("Scanner","nooooo")
                }
            }
        }
    }

    private fun codeScanner(routingView: ActivityScannerBinding){
        codeScanner = CodeScanner(this,routingView.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback =  DecodeCallback {
                runOnUiThread {
                    model.getBook(it.text)
                    Log.i("scanner","the code ${it.text}")
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread{
                    Log.e("scanner","Erro message ${it.message}")
                }
            }
        }

        codeScanner.startPreview()
        routingView.scannerView.setOnClickListener(){
            codeScanner.startPreview()
        }
    }
}