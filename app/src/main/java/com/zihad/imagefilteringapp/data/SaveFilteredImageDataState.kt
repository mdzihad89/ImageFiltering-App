package com.zihad.imagefilteringapp.data

import android.net.Uri

data class SaveFilteredImageDataState(
    val isLoading: Boolean,
    val uri: Uri?,
    val error: String?
)
