package com.example.designuilogin

import android.icu.text.Transliterator.Position
import android.media.Image

interface OnImageLongClickListener {
    fun onImageLongClick(imagePath: String,position: Int )
}