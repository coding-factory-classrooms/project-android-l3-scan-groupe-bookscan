package com.coding.bookscan.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class MainViewModelState(open val errorMessage : String = ""){
    data class Success(val canLoadScanner : Boolean) : MainViewModelState()
    data class Failure(override val errorMessage: String): MainViewModelState()
}
class MainViewModel : ViewModel() {
    private val mainState = MutableLiveData<MainViewModelState>()

    fun getMainState(): MutableLiveData<MainViewModelState> {
        return mainState
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkInternetConnection(context: Context){
        if(isActiveInternetConnection(context)){
            mainState.postValue(MainViewModelState.Success(true));
        }else{
            mainState.postValue(MainViewModelState.Failure("L'application nécessite une connexion à internet pour fonctionner"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isActiveInternetConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }
}