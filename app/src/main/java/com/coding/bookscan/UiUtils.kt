package com.coding.bookscan

import com.coding.bookscan.entity.data.Book

fun Book.formatedImgName(): String {
    val imgNameSplited = this.image.split(".png")
    return imgNameSplited[0]
}