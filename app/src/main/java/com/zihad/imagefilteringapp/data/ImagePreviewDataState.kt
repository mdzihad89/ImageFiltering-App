package com.zihad.imagefilteringapp.data

import android.graphics.Bitmap

data class ImagePreviewDataState(
    val isLoading: Boolean,
    val bitmap: Bitmap?,
    val error: String?
    )
