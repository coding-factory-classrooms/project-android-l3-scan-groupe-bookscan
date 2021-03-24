package com.coding.bookscan.activity
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.CodeScanner
import com.coding.bookscan.databinding.ActivityScannerBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

private const val CAMERA_REQUEST_CODE = 101


class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var codeScanner: CodeScanner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codeScanner(binding)
    }

    private fun codeScanner(routingView: ActivityScannerBinding){
        codeScanner = CodeScanner(this,routingView.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback =  DecodeCallback {
                runOnUiThread {
                    routingView.scannerTextView.text = it.text
                    Log.i("scanner","the code ${it.text}")
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Scanner","Erro message ${it.message}")
                }
            }
        }
        codeScanner.startPreview()
        routingView.scannerView.setOnClickListener(){
            codeScanner.startPreview()
        }
    }

    override fun onRestart() {
        super.onRestart()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions(){
        val permission:Int = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED){
            Log.i("scanner","")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
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


}