package com.zihad.imagefilteringapp.repository

import android.graphics.Bitmap
import android.net.Uri
import com.zihad.imagefilteringapp.data.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri?
}