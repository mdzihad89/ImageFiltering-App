package com.zihad.imagefilteringapp.data


data class ImageFilterDataState(
    val isLoading: Boolean,
    val imageFilters: List<ImageFilter>?,
    val error: String?
)
