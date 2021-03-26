package com.coding.bookscan

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.coding.bookscan.entity.data.Book
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Book.formatedImgName(): String {
    val imgNameSplited = this.image.split(".png")
    return imgNameSplited[0]
}

@RequiresApi(Build.VERSION_CODES.O)
fun Book.formatedDate() : String{
    val splitedDate = this.scanDate.split(" ")
    Log.i("dateFormat", "formatedDate: $splitedDate")
    return  "${splitedDate[2]} ${splitedDate[1]} ${splitedDate[5]} - ${splitedDate[3]}"
}

