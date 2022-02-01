package com.zihad.imagefilteringapp.listeners

import java.io.File

interface SavedImageListener {
    fun onImageClicked(file: File)
}