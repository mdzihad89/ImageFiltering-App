package com.zihad.imagefilteringapp.listeners

import com.zihad.imagefilteringapp.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}