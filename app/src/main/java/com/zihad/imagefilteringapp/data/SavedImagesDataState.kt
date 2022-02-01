package com.zihad.imagefilteringapp.data

import android.graphics.Bitmap
import java.io.File

data class SavedImagesDataState(
    val isLoading: Boolean,
    val savedImages: List<Pair<File, Bitmap>>?,
    val error: String?
)
