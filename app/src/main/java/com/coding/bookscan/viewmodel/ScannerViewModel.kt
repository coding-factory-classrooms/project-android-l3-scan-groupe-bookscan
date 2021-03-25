package com.coding.bookscan.viewmodel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.coding.bookscan.databinding.ActivityScannerBinding
import com.coding.bookscan.entity.data.Book
import kotlin.concurrent.thread

const val CAMERA_REQUEST_CODE = 101

sealed class ScannerViewModelState(open val errorMessage : String = "", open val successMessage : String = "") {
    data class Success(val book : Book, override val successMessage: String) : ScannerViewModelState(successMessage = successMessage)
    data class Failure(override val errorMessage: String) : ScannerViewModelState(errorMessage = errorMessage)
}

class ScannerViewModel : ViewModel() {

    private val scannerState = MutableLiveData<ScannerViewModelState>()

    fun getScannerState(): MutableLiveData<ScannerViewModelState> {
        return scannerState
    }

    private fun makeRequest(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    fun setupPermissions(context: Context, activity: Activity){
        val permission:Int = ContextCompat.checkSelfPermission(context,android.Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED){
            Log.i("scanner","")
            makeRequest(activity)
        }
    }

    fun getBook(isbn : String){
        val book = Book(0,isbn,"","","","","","","","",0)
        scannerState.postValue(ScannerViewModelState.Success(book,"Livre trouvé $isbn"))
    }
}